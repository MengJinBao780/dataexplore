var nowTemp = new Date();
var now = new Date(nowTemp.getFullYear(), nowTemp.getMonth(), nowTemp.getDate(), 0, 0, 0, 0);
var checkin = $("#browse-inputDate-start").datepicker({
}).on('changeDate', function(ev) {
    var newDate = new Date(ev.date)
    newDate.setDate(newDate.getDate() + 1);
    checkout.setValue(newDate);
    checkin.hide();
}).data('datepicker');
var checkout = $("#browse-inputDate-end").datepicker({
    onRender: function(date) {
        return date.valueOf() <= checkin.date.valueOf() ? 'disabled' : '';
    }
}).on('changeDate', function(ev) {
    checkout.hide();
}).data('datepicker');

var Seacheckin = $("#search-inputDate-start").datepicker({
}).on('changeDate', function(ev) {
    var newDate = new Date(ev.date)
    newDate.setDate(newDate.getDate() + 1);
    Seacheckout.setValue(newDate);
    Seacheckin.hide();
}).data('datepicker');
var Seacheckout = $("#search-inputDate-end").datepicker({
    onRender: function(date) {
        return date.valueOf() <= Seacheckin.date.valueOf() ? 'disabled' : '';
    }
}).on('changeDate', function(ev) {
    Seacheckout.hide();
}).data('datepicker');

var Colcheckin = $("#collection-inputDate-start").datepicker({
}).on('changeDate', function(ev) {
    var newDate = new Date(ev.date)
    newDate.setDate(newDate.getDate() + 1);
    Colcheckout.setValue(newDate);
    Colcheckin.hide();
}).data('datepicker');
var Colcheckout = $("#collection-inputDate-end").datepicker({
    onRender: function(date) {
        return date.valueOf() <= Colcheckin.date.valueOf() ? 'disabled' : '';
    }
}).on('changeDate', function(ev) {
    Colcheckout.hide();
}).data('datepicker');

var Dowcheckin = $("#download-inputDate-start").datepicker({
}).on('changeDate', function(ev) {
    var newDate = new Date(ev.date)
    newDate.setDate(newDate.getDate() + 1);
    Dowcheckout.setValue(newDate);
    Dowcheckin.hide();
}).data('datepicker');
var Dowcheckout = $("#download-inputDate-end").datepicker({
    onRender: function(date) {
        return date.valueOf() <= Dowcheckin.date.valueOf() ? 'disabled' : '';
    }
}).on('changeDate', function(ev) {
    Dowcheckout.hide();
}).data('datepicker');