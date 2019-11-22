$(function() {
    doLocale();
    bindSelect();
    renderModeCenters();
    validate();
    dealModeCenterModal();
    handleFieldValidator();
    submitModeCenter();
    bindButtons();
});

function renderModeCenters() {
    $("#add-mode-config").hide();
    $("#mode-centers").bootstrapTable({
        url: "/modeConfig/load",
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true,
        queryParams: {"projectId": $("#project-query").val()}
    }).on("all.bs.table", function() {
        doLocale();
    });
}

/**
 * 修改值
 */
function reload() {
  var projectId = $("#project-query").val();
  var opt = {
    url: "/modeConfig/load",
    query:{
      "projectId":projectId
    }
  };
  $("#add-mode-config").hide();
  $("#mode-centers").bootstrapTable("refresh", opt);
}

/**
 * 格式化状态
 *
 * @param value
 * @param row
 * @returns {string}
 */
function statusFormatter(value, row) {
  switch(value) {
    case 1:
      return "<span class='label label-success' data-lang='status-open'></span>";
      break;
    case 0:
      return "<span class='label label-warning' data-lang='status-close'></span>";
      break;
    default:
      return "<span class='label label-warning' data-lang='status-close'></span>";
      break;
  }
}

function generateOperationButtons(val, row) {
    if (row.roleId == 4) {
      return "-";
    }
    $("#add-mode-config").show();
    var viewTd = "<button operation='view-mode-center' class='btn-xs btn-info' data-toggle='modal' id='view-dialog' modeId='" + row.id + "' data-lang='operation-view'></button>";
    var operationTd = "<button operation='edit-mode-center' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' modeId='" + row.id + "' data-lang='operation-update'></button>";
    return viewTd + operationTd;
}

function bindButtons() {
    bindUpdateButtons();
    bindViewButtons();
}

function bindUpdateButtons() {
    $(document).off("click", "button[operation='edit-mode-center']");
    $(document).on("click", "button[operation='edit-mode-center']", function(event) {
        fillGroupConfig();
        var modeId = $(event.currentTarget).attr("modeId");
        $.ajax({
            url: "/modeConfig/getModeConfig",
            type: "POST",
            data: JSON.stringify({"id" : modeId}),
            contentType: "application/json",
            dataType: "json",
            success: function(d) {
                if (d && d.data) {
                  var data = d.data;
                  if (data) {
                    $("#add-mode-center").modal({backdrop: 'public', keyboard: true});
                    $("#modeId").val(data.id);
                    $("#modeName").val(data.name);
                    $("#alarmSwitch").val(data.alarmSwitch);
                    $("#mailAlarmSwitch").val(data.mailAlarmSwitch);
                    $("#mailTitle").val(data.mailTitle);
                    $("#mailContent").val(data.mailContent);
                    $("#smsAlarmSwitch").val(data.smsAlarmSwitch);
                    $("#smsContent").val(data.smsContent);
                    $("#dingAlarmSwitch").val(data.dingAlarmSwitch);
                    $("#dingUrl").val(data.dingUrl);
                    $("#dingContent").val(data.dingContent);
                    $("#alarmClientSwitch").val(data.alarmClientSwitch);
                    $("#alarmServerSwitch").val(data.alarmServerSwitch);
                    $("#remark").val(data.remark);
                    $("#alarmGroup").val(data.alarmGroupId);
                  }
                }
            }
        });
    });
}

function bindViewButtons() {
  $(document).off("click", "button[operation='view-mode-center']");
  $(document).on("click", "button[operation='view-mode-center']", function(event) {
    fillGroupConfig();
    var modeId = $(event.currentTarget).attr("modeId");
    $.ajax({
      url: "/modeConfig/getModeConfig",
      type: "POST",
      data: JSON.stringify({"id" : modeId}),
      contentType: "application/json",
      dataType: "json",
      success: function(d) {
        if (d && d.data) {
          var data = d.data;
          if (data) {
            $("#view-mode-center").modal({backdrop: 'public', keyboard: true});
            $("#viewModeName").val(data.name);
            $("#viewAppName").val(data.appName);
            $("#viewMailTitle").val(data.mailTitle);
            $("#viewMailContent").val(data.mailContent);
            $("#viewSmsContent").val(data.smsContent);
            $("#viewDingUrl").val(data.dingUrl);
            $("#viewDingContent").val(data.dingContent);
            $("#viewRemark").val(data.remark);
          }
        }
      }
    });
  });
}

function dealModeCenterModal() {
    $("#add-mode-config").click(function() {
        fillGroupConfig();
        $("#add-mode-center").modal({backdrop: 'public', keyboard: true});
        var defaultContent = $.i18n.prop("default-alarm-content");
        $("#modeId").val("");
        $("#mailTitle").val($.i18n.prop("default-alarm-mail-title"));
        $("#mailContent").val(defaultContent);
        $("#smsContent").val(defaultContent);
        $("#dingContent").val(defaultContent);
        $("#smsAlarmSwitch").val(0);
        $("#alarmClientSwitch").val(0);
    });
    $("#close-add-reg-form").click(function() {
        $("#add-mode-center").on("hide.bs.modal", function () {
            $("#mode-center-form")[0].reset();
        });
        $("#mode-center-form").data("bootstrapValidator").resetForm();
    });
    $("#close-view-reg-form").click(function() {
      $("#view-mode-center").on("hide.bs.modal", function () {
        $("#view-center-form")[0].reset();
      });
      $("#view-center-form").data("bootstrapValidator").resetForm();
    });
}

function fillGroupConfig() {
  //设置报警组
  $.ajax({
    url: "/modeConfig/getAlarmGroup",
    type: "GET",
    async: false,
    dataType: "json",
    success: function(d) {
      if (d && d.data) {
        var data = d.data;
        if (data) {
          var html = "";
          for(var i=0;i<data.length;i++){
            html += "<option value="+data[i].id+">"+data[i].name+"</option>"
          }
          $("#alarmGroup").html(html);
        }
      }
    }
  });

  //设置应用
  $.ajax({
    url: "/appConfig/getAuthApps",
    type: "GET",
    async: false,
    dataType: "json",
    success: function(d) {
      if (d && d.data) {
        var data = d.data;
        if (data) {
          var html = "";
          for(var i=0;i<data.length;i++){
            html += "<option value="+data[i].id+">"+data[i].appName+"</option>"
          }
          $("#appName").html(html);
        }
      }
    }
  });
}

function handleFieldValidator() {
    $("#modeName").focus(function() {
        $("#mode-center-form").data("bootstrapValidator").enableFieldValidators("modeName", true);
    });
}

function submitModeCenter() {
    $("#add-mode-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#mode-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var modeId = $("#modeId").val();
            var modeName = $("#modeName").val();
            var alarmGroup = $("#alarmGroup").val();
            var alarmSwitch = $("#alarmSwitch").val();
            var mailAlarmSwitch = $("#mailAlarmSwitch").val();
            var mailTitle = $("#mailTitle").val();
            var mailContent = $("#mailContent").val();
            var smsAlarmSwitch = $("#smsAlarmSwitch").val();
            var smsContent = $("#smsContent").val();
            var dingAlarmSwitch = $("#dingAlarmSwitch").val();
            var dingUrl = $("#dingUrl").val();
            var dingContent = $("#dingContent").val();
            var alarmClientSwitch = $("#alarmClientSwitch").val();
            var alarmServerSwitch = $("#alarmServerSwitch").val();
            var remark = $("#remark").val();
            var appId = $("#appName").val();
            $.ajax({
                url: "/modeConfig/addAndUpdate",
                type: "POST",
                data: JSON.stringify({"id": modeId, "name": modeName, "alarmGroup": alarmGroup, "alarmSwitch": alarmSwitch, "mailAlarmSwitch": mailAlarmSwitch, "remark": remark, "mailTitle": mailTitle
                      ,"smsAlarmSwitch":smsAlarmSwitch, "dingAlarmSwitch":dingAlarmSwitch,"mailContent":mailContent,"smsContent":smsContent,"dingContent":dingContent,
                      "dingUrl":dingUrl,"alarmClientSwitch":alarmClientSwitch,"alarmServerSwitch":alarmServerSwitch, "appId": appId}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data) {
                        $("#add-mode-center").on("hide.bs.modal", function() {
                            $("#mode-center-form")[0].reset();
                        });
                        $("#mode-center-form").data("bootstrapValidator").resetForm();
                        $("#add-mode-center").modal("hide");
                        reload();
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                    }
                }
            });
        }
    });
}

function validate() {
    $("#mode-center-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
            modeName: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("alarm-mode-name-not-null")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("app-config-name-length-limit")
                    },
                    callback: {
                        message: $.i18n.prop("alarm-mode-name-existed"),
                        callback: function() {
                            var modeName = $("#modeName").val();
                            var id = $("#modeId").val();
                            var result = true;
                            $.ajax({
                                url: "/modeConfig/existed",
                                type: "POST",
                                data: JSON.stringify({"name": modeName, "id": id}),
                                contentType: "application/json",
                                async: false,
                                success: function(d) {
                                  if (d.data < 1) {
                                      result = true;
                                    } else {
                                      result = false;
                                    }
                                }
                            });
                            return result;
                        }
                    }
                }
            }
        }
    });
    $("#mode-center-form").submit(function(event) {
        event.preventDefault();
    });
}
