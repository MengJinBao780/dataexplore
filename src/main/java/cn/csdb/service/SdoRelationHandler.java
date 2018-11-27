package cn.csdb.service;

import cn.csdb.model.Frequencies;
import cn.csdb.model.Sdo;
import cn.csdb.model.SdoRelate;
import cn.csdb.model.SdoRelation;
import cn.csdb.repository.SdoDao;
import cn.csdb.repository.SdoRelationDao;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;

@Service
public class SdoRelationHandler {

    private Logger logger = LoggerFactory.getLogger(SdoRelationHandler.class);

    private static final double publishTimeWeights = 0.1;
    private static final double titleWeights = 0.4;
    private static final double descWeights = 0.3 * 0.483;
    private static final double keywordsWeights = 0.3 * 0.517;
    private static final double organizationWeights = 0.2;
    private static final double minScore = 0.35; //存储最小分值

    @Resource
    private SdoRelationDao sdoRelationDao;
    @Resource
    private SdoDao sdoDao;

    /**
     * 定时任务配置在applicationContext.xml，每天晚上凌晨执行
     *
     */
    public void executeData() {
        logger.info("开始执行关联数据定时任务------------");
        boolean isNeedUpdateSdoRelationTask = isNeedUpdateSdoRelationTask();
        if (isNeedUpdateSdoRelationTask) {
            logger.info("sdo需要更新------------start");
            updateSdoRelationTask();
            logger.info("sdo需要更新------------end");
        }
        boolean isNeedCreateSdoRelationTask = isNeedCreateSdoRelationTask();
        if (isNeedCreateSdoRelationTask) {
            logger.info("新的sdo------------start");
            createSdoRelationTask();
            logger.info("新的sdo------------end");
        }
        logger.info("结束执行关联数据定时任务-------------");
    }

    private boolean isNeedUpdateSdoRelationTask() {
        Date minCreateTimeRelate = sdoRelationDao.getMinCreateTime();
        if (minCreateTimeRelate == null) {
            return false;
        }
        Date maxCreateTime = sdoDao.getMaxCreateTime();
        return maxCreateTime.after(minCreateTimeRelate);
    }

    private boolean isNeedCreateSdoRelationTask() {
        Date maxLastCompleteCreateTime = sdoRelationDao.getMaxLastCompleteCreateTime();
        if (maxLastCompleteCreateTime == null) {
            return true;
        }
        Date maxCreateTime = sdoDao.getMaxCreateTime();
        return maxCreateTime.after(maxLastCompleteCreateTime);
    }

    private void updateSdoRelationTask() {
        /**
         * 1、遍历获取relation——sdo-list(时间正序，每次取0-10条)
         * 2、按照relation-sdo的CreateTime获取sdo的createTime大于CreateTime ，sdo-list(时间正序，每次10条)
         * 3、计算相似度分值
         * 4、更新CreateTime
         */
        int pageSize = 10;
        while (true) {
            List<SdoRelation> srcSdoRelationList = sdoRelationDao.getByPage(0, pageSize);//分页获取sdoRelation-List,按照create_time正序获取
            for (SdoRelation sdoRelation : srcSdoRelationList) {
                long tarCount = sdoDao.getCountAfterCreateTime(sdoRelation.getCreateTime());
                if (tarCount == 0) {//如果为0，说明没有要更新的sdo；由于按照create_time正序获取，所以之后的sdoRelation也不需要再更新
                    return;
                }
                Sdo srcSdo = sdoDao.getSdoById(sdoRelation.getSdoId());
                if (srcSdo == null) {
                    continue;
                }
                TreeSet<SdoRelate> sdoRelateSet = Sets.newTreeSet(sdoRelation.getSdoRelates());
                List<String> srcDescTerms = getTerms(srcSdo.getDesc(), true);
                List<String> srcTitleTerms = getTerms(srcSdo.getTitle(), false);
                Date srcPublisherPublishTime = srcSdo.getPublisherPublishTime();
                String srcOrganization = srcSdo.getPublisher().getOrganization();
                String[] srcKeywordTerms = StringUtils.split(srcSdo.getKeyword(), ",");
                long tarOffset = 0;
                long tarCurSize = pageSize;
                while (tarOffset < tarCount) {
                    if ((tarOffset + pageSize) >= tarCount) {
                        tarCurSize = tarCount - tarOffset;
                    }
                    List<Sdo> targetSdoList = sdoDao.getSdoList(sdoRelation.getCreateTime(), tarOffset, tarCurSize);//分页获取tarSdo-list，按照create_time正序排序
                    executeSdoRelation(sdoRelation, sdoRelateSet,
                            srcDescTerms, srcTitleTerms, srcPublisherPublishTime, srcOrganization, srcKeywordTerms,
                            targetSdoList);//计算srcSdo与targetSdoList关联关系
                    tarOffset += pageSize;
                }
                sdoRelation.setSdoRelates(Lists.newArrayList(sdoRelateSet));
                sdoRelationDao.save(sdoRelation);//保存sdoRelation到数据库
            }
        }


    }

    private void createSdoRelationTask() {
        /**
         * 1、获取realtion-sdo,最大createTime
         * 2、获取大于createTime的sdo-list(时间正序，每次10条)
         * 3、计算相似度分值
         * 4、写入lastCreateTime
         */
        Date maxLastCompleteCreateTime = sdoRelationDao.getMaxLastCompleteCreateTime();
        long count = sdoDao.getCountAfterCreateTime(maxLastCompleteCreateTime);
        int pageSize = 10;
        long offset = 0;
        long curSize = pageSize;
        while (offset < count) {
            if ((offset + pageSize) >= count) {
                curSize = count - offset;
            }
            List<Sdo> srcSdoList = sdoDao.getSdoList(maxLastCompleteCreateTime, offset, curSize);//分页获取srcSdo-list
            for (Sdo srcSdo : srcSdoList) {//遍历srcSdo，计算关联资源
                SdoRelation sdoRelation = new SdoRelation();
                sdoRelation.setSdoId(srcSdo.getId());//srcSdo的sdoId
                sdoRelation.setLastCreateTime(srcSdo.getCreateTime());//srcSdo的create_time
                TreeSet<SdoRelate> sdoRelateSet = Sets.newTreeSet();
                List<String> srcDescTerms = getTerms(srcSdo.getDesc(), true);
                List<String> srcTitleTerms = getTerms(srcSdo.getTitle(), false);
                Date srcPublisherPublishTime = srcSdo.getPublisherPublishTime();
                String srcOrganization = srcSdo.getPublisher().getOrganization();
                String[] srcKeywordTerms = StringUtils.split(srcSdo.getKeyword(), ",");
                long tarCount = sdoDao.getCount();
                long tarOffset = 0;
                long tarCurSize = pageSize;
                while (tarOffset < tarCount) {
                    if ((tarOffset + pageSize) >= tarCount) {
                        tarCurSize = count - tarOffset;
                    }
                    List<Sdo> targetSdoList = sdoDao.getSdoList(tarOffset, tarCurSize);//分页获取tarSdo-list
                    executeSdoRelation(sdoRelation, sdoRelateSet,
                            srcDescTerms, srcTitleTerms, srcPublisherPublishTime, srcOrganization, srcKeywordTerms,
                            targetSdoList);//计算srcSdo与targetSdoList关联关系
                    tarOffset += pageSize;
                }
                sdoRelation.setSdoRelates(Lists.newArrayList(sdoRelateSet));
                sdoRelationDao.save(sdoRelation);//保存sdoRelation到数据库
            }
            offset += pageSize;
        }

    }

    private void executeSdoRelation(SdoRelation sdoRelation,
                                    TreeSet<SdoRelate> sdoRelateSet,
                                    List<String> srcDescTerms, List<String> srcTitleTerms, Date srcPublisherPublishTime,
                                    String srcOrganization, String[] srcKeywordTerms,
                                    List<Sdo> targetSdoList) {
        for (Sdo tarSdo : targetSdoList) { //遍历tarSdo，计算其与srcSdo的分值
            if (StringUtils.equals(sdoRelation.getSdoId(), tarSdo.getId())) {
                continue;
            }
            sdoRelation.setCreateTime(tarSdo.getCreateTime());
            double score = executeScore(tarSdo,
                    srcDescTerms, srcTitleTerms, srcPublisherPublishTime, srcOrganization, srcKeywordTerms);//计算分值
//            if (sdoRelateSet.size() >= 20) { //set只保存，分值top20的关联sdo
//                if (score > sdoRelateSet.first().getScore()) {
//                    sdoRelateSet.pollFirst();
//                } else {
//                    continue;
//                }
//            }
            if (score < minScore) {//如果小于最小分值，则不进行存储
                continue;
            }
            SdoRelate relate = new SdoRelate();
            relate.setScore(score);
            relate.setSdoId(tarSdo.getId());
            relate.setSdoTitle(tarSdo.getTitle());
            sdoRelateSet.add(relate);
        }
    }

    private double executeScore(Sdo tarSdo,
                                List<String> srcDescTerms, List<String> srcTitleTerms, Date srcPublisherPublishTime,
                                String srcOrganization, String[] srcKeywordTerms) {
        List<String> termsTitleTar = getTerms(tarSdo.getTitle(), false);
        double titleScore = getScore(srcTitleTerms, termsTitleTar);

        List<String> termsDescTar = getTerms(tarSdo.getDesc(), true);
        double descScore = getScore(srcDescTerms, termsDescTar);

        double publishTimeScore = getTimeScore(srcPublisherPublishTime, tarSdo.getPublisherPublishTime());

        double organizationScore = getOrganizationScore(srcOrganization, tarSdo.getPublisher().getOrganization());

        String[] keywordsTar = StringUtils.split(tarSdo.getKeyword(), ",");
        double keywordsScore = getScore(Lists.newArrayList(srcKeywordTerms), Lists.newArrayList(keywordsTar));

        return getComplexScore(publishTimeScore, titleScore, descScore,
                keywordsScore, organizationScore);
    }

    private double getComplexScore(double publishTimeScore, double titleScore, double descScore,
                                   double keywordsScore, double organizationScore) {
        return publishTimeScore * publishTimeWeights + titleScore * titleWeights
                + descScore * descWeights + keywordsScore * keywordsWeights
                + organizationScore * organizationWeights;
    }

    /**
     * 获取来源分数
     *
     * @return
     */

    private double getOrganizationScore(String organizationSrc, String organizationTar) {
        return StringUtils.equalsIgnoreCase(organizationSrc, organizationTar) ? 1 : 0;
    }

    /**
     * 时间分数
     *
     * @param timeSrc
     * @param timeTar
     * @return
     */
    private double getTimeScore(Date timeSrc, Date timeTar) {
        long days = (timeSrc.getTime() - timeTar.getTime()) / (24 * 60 * 60 * 1000);
        days = days < 0 ? (0 - days) : days;
        return days == 0 ? 1 : 1.0 / days;
    }

    /**
     * 短语余弦相似度分数
     *
     * @param termsSrc
     * @param termsTar
     * @return
     */
    private double getScore(List<String> termsSrc, List<String> termsTar) {
        HashMap<String, Frequencies> map = Maps.newHashMap();
        for (String src : termsSrc) {
            Frequencies frequencies = map.get(src);
            if (frequencies == null) {
                map.put(src, new Frequencies(1, 0));
            } else {
                frequencies.addSrc();
            }
        }
        for (String tar : termsTar) {
            Frequencies frequencies = map.get(tar);
            if (frequencies == null) {
                map.put(tar, new Frequencies(0, 1));
            } else {
                frequencies.addTar();
            }
        }
        Collection<Frequencies> frequenciesCol = map.values();
        return getScore(frequenciesCol);
    }

    /**
     * 余弦相似度分数
     *
     * @param frequenciesCol
     * @return
     */
    private double getScore(Collection<Frequencies> frequenciesCol) {
        double i = 0;
        double j = 0;
        for (Frequencies f : frequenciesCol) {
            i += f.getSrc() * f.getTar();
            j += Math.pow(Math.pow(f.getSrc(), 2) + Math.pow(f.getTar(), 2), 1.0 / 2);
        }
        return j == 0 ? 0 : (i / j);
    }

    /**
     * 短语分词
     *
     * @param text
     * @param useSmart，当为true时，分词器进行最大词长切分
     * @return
     */
    List<String> getTerms(String text, boolean useSmart) {
        IKSegmenter ik = new IKSegmenter(new StringReader(text), useSmart);
        ArrayList<String> list = Lists.newArrayList();
        try {
            Lexeme lexeme;
            while ((lexeme = ik.next()) != null) {
                list.add(lexeme.getLexemeText());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

}
