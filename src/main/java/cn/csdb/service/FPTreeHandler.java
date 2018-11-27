package cn.csdb.service;

import cn.csdb.fpgrowth.FPTree;
import cn.csdb.fpgrowth.MapLinked;
import cn.csdb.fpgrowth.RelationModel;
import cn.csdb.model.SdoVisit;
import cn.csdb.repository.RelationModelDao;
import cn.csdb.repository.SdoVisitDao;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by ajian on 2018/5/24.
 */
@Service
public class FPTreeHandler {

    private Logger logger = LoggerFactory.getLogger(FPTreeHandler.class);
    @Resource
    private SdoVisitDao sdoVisitDao;

    @Resource
    private RelationModelDao relationModelDao;

    private static final int MIN_SUPORT = 1;
    private static final int DAYS = 30;
    private  final  static  int maxCount = 20; //最大条数
    public void execute() {

        logger.info("定时任务开始：用户访问记录，关联发现--------start");
        List<List<String>> allData = Lists.newArrayList();
        List<String> loginIdList = sdoVisitDao.getAllLoginId();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtils.addHours(new Date(), 8));
        calendar.add(Calendar.DATE, -DAYS);
        for (String loginId : loginIdList) {
            List<SdoVisit> sdoVisitList = sdoVisitDao.getSdoVisits(loginId, calendar.getTime());
            List<String> sdoList = Lists.transform(sdoVisitList, new Function<SdoVisit, String>() {
                @Override
                public String apply(SdoVisit sdoVisit) {
                    return sdoVisit.getSdoId();
                }
            });
            allData.add(sdoList);
        }
        MapLinked<String> mapLinked = new MapLinked(maxCount);
        FPTree fptree = new FPTree();
        fptree.setMinSuport(MIN_SUPORT);
        fptree.FPGrowth(allData, null, mapLinked);
        System.out.println("-----------------------------");
        Collection<RelationModel> relationModels = mapLinked.values();
        for (RelationModel relationModel : relationModels) {
            relationModel.setRelations(Lists.newArrayList(relationModel.getRelationSet()));
            relationModel.setRelationSet(null);
            relationModelDao.save(relationModel);
//            System.out.print(relationModel.getCount() + "\t" + relationModel.getSdoId() + "\t");
//            for (String s : relationModel.getRelations()) {
//                System.out.printf(s + "\t");
//            }
//            System.out.println();
        }
        System.out.println(relationModels.size());
        logger.info("定时任务结束：用户访问记录，关联发现--------end");
    }


}
