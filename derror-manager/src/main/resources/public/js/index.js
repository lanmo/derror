$(function () {
  //权限,项目管理
  $("#project-center").click(function () {
    $("#content").load("/auth/project_center/page");
  });
  //用户管理
  $("#user-center").click(function () {
    $("#content").load("/auth/user_center/page");
  });
  $("#content").load("/query/query_exception/page");
  $("#reg-center").click(function () {
    $("#content").load("/global/appconfig_center/page");
  });
  $("#alarm-mode").click(function () {
    $("#content").load("/global/alarm_mode/page");
  });
  $("#filter-exception").click(function () {
    $("#content").load("/global/filter_exception/page");
  });
  $("#alarm-group").click(function () {
    $("#content").load("/global/alarm_group/page");
  });
  $("#alarm-group-member").click(function () {
    $("#content").load("/global/alarm_group_member/page");
  });
  $("#help").click(function () {
    $("#content").load("/help/help/page", null, function () {
      doLocale();
    });
  });
  $("#query-exception").click(function () {
    $("#content").load("/query/query_exception/page");
  });
  $("#alarm-mail").click(function () {
    $("#content").load("/global/alarm_mail/page");
  });
  $("#exception-statistics").click(function () {
    $("#content").load("/query/statistics/page");
  });
  switchLanguage();
});
