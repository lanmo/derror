$(function() {
    doLocale();
    bindSelect();
    renderRegCenters();
    validate();
    dealAppCenterModal();
    handleFieldValidator();
    submitRegCenter();
    bindButtons();
});

function renderRegCenters() {
    $("#add-mode-config").hide();
    $("#reg-centers").bootstrapTable({
        url: "/appConfig/load",
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true,
        queryParams: {"projectId": $("#project-query").val()},
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
    url: "/appConfig/load",
    query:{
      "projectId":projectId
    }
  };
  $("#add-app-config").hide();
  $("#reg-centers").bootstrapTable("refresh", opt);
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

/**
 * 格式化频次
 *
 * @param value
 * @param row
 * @returns {string}
 */
function frequencyFormat(value, row) {
    var val = value + $.i18n.prop("frequency");
    return val;
}

/**
 * 格式化频次
 *
 * @param value
 * @param row
 * @returns {string}
 */
function retainFormat(value, row) {
  var val = value + $.i18n.prop("retain");
  return val;
}

/**
 * 格式化频次
 *
 * @param value
 * @param row
 * @returns {string}
 */
function frequencyTimeFormat(value, row) {
  var val = value + $.i18n.prop("frequencyTimeFormat");
  return val;
}

function generateOperationButtons(val, row) {
    if (row.roleId == 4) {
      return "-";
    }
    $("#add-app-config").show();
    var operationTd;
    var name = row.appName;
    if (row.status == 1) {
        operationTd = "<button operation='close-app-config' class='btn-xs btn-info' appName='" + name + "' status='" + row.status + "' data-lang='status-close'></button>&nbsp;<button operation='edit-reg-center' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' appName='" + name + "' appId='" + row.id + "' data-lang='operation-update'></button>";
    } else if (row.status == 0) {
        operationTd = "<button operation='close-app-config' class='btn-xs btn-info' appName='" + name + "' status='" + row.status + "' data-lang='status-open'></button>&nbsp;<button operation='edit-reg-center' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' appName='" + name + "' appId='" + row.id + "' data-lang='operation-update'></button>";
    }
    return operationTd;
}

function bindButtons() {
    bindOpenButtons();
    bindUpdateButtons();
}

function bindOpenButtons() {
    $(document).off("click", "button[operation='close-app-config']");
    $(document).on("click", "button[operation='close-app-config']", function(event) {
        var status = $(event.currentTarget).attr("status");
        var msg = $.i18n.prop("confirm-to-opened");
        if (status == 1) {
          msg = $.i18n.prop("confirm-to-closed");
        }
        showCloseConfirmModal(msg);
        var appName = $(event.currentTarget).attr("appName");
        $(document).off("click", "#confirm-btn");
        $(document).on("click", "#confirm-btn", function() {
         $.ajax({
          url: "/appConfig/changeStatus",
          type: "POST",
          data: JSON.stringify({"appName" : appName, "status" : status}),
          contentType: "application/json",
          dataType: "json",
          success: function(data) {
            $("#confirm-dialog").modal("hide");
            $(".modal-backdrop").remove();
            $("body").removeClass("modal-open");
            if (data) {
              $("#reg-centers").bootstrapTable("refresh");
              showSuccessDialog();
            }
          }
        });
      });
    });
}

function bindUpdateButtons() {
    $(document).off("click", "button[operation='edit-reg-center']");
    $(document).on("click", "button[operation='edit-reg-center']", function(event) {
        fillModeConfig();
        var appId = $(event.currentTarget).attr("appId");
        $.ajax({
            url: "/appConfig/getAppConfig",
            type: "POST",
            data: JSON.stringify({"id" : appId}),
            contentType: "application/json",
            dataType: "json",
            success: function(d) {
                if (d && d.data) {
                  var data = d.data;
                  if (data) {
                    $("#add-reg-center").modal({backdrop: 'public', keyboard: true});
                    $("#appName").val(data.appName).attr("readonly", "readonly");
                    $("#projectName").val(data.projectId).attr("readonly", "readonly");
                    $("#retain").val(data.retain);
                    $("#frequency").val(data.frequency);
                    $("#frequencyTime").val(data.frequencyTime);
                    $("#remark").val(data.remark);
                    $("#appId").val(data.id);
                  }
                }
            }
        });
    });
}

function dealAppCenterModal() {
    $("#add-app-config").click(function() {
        fillModeConfig();
        $("#add-reg-center").modal({backdrop: 'public', keyboard: true});
        $("#appName").removeAttr("readonly");
        $("#appId").val("");
    });
    $("#close-add-reg-form").click(function() {
      $("#add-reg-center").on("hide.bs.modal", function () {
        $("#reg-center-form")[0].reset();
      });
      $("#reg-center-form").data("bootstrapValidator").resetForm();
    });
}

function fillModeConfig() {
  $.ajax({
    url: "/project/getAuthProjects",
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
          $("#projectName").html(html);
        }
      }
    }
  });
}

function handleFieldValidator() {
    $("#appName").focus(function() {
        $("#reg-center-form").data("bootstrapValidator").enableFieldValidators("appName", true);
    });
}

function submitRegCenter() {
    $("#add-reg-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#reg-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var appId = $("#appId").val();
            var appName = $("#appName").val();
            var alarmModel = $("#alarmModel").val();
            var retain = $("#retain").val();
            var frequency = $("#frequency").val();
            var frequencyTime = $("#frequencyTime").val();
            var remark = $("#remark").val();
            var projectId = $("#project-query").val()
            $.ajax({
                url: "/appConfig/addAndUpdate",
                type: "POST",
                data: JSON.stringify({"appName": appName, "alarmModel": alarmModel, "retain": retain, "frequency": frequency, "frequencyTime": frequencyTime, "remark": remark, "id": appId, "projectId":projectId}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data) {
                        $("#add-reg-center").on("hide.bs.modal", function() {
                            $("#reg-center-form")[0].reset();
                        });
                        $("#reg-center-form").data("bootstrapValidator").resetForm();
                        $("#add-reg-center").modal("hide");
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
    $("#reg-center-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
            appName: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("app-config-name-not-null")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("app-config-name-length-limit")
                    },
                    callback: {
                        message: $.i18n.prop("app-config-name-existed"),
                        callback: function() {
                            var appName = $("#appName").val();
                            var id = $("#appId").val();
                            var result = true;
                            $.ajax({
                                url: "/appConfig/existed",
                                type: "POST",
                                data: JSON.stringify({"appName": appName, "id": id}),
                                contentType: "application/json",
                                async: false,
                                success: function(data) {
                                  if (data.data < 1) {
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
    $("#reg-center-form").submit(function(event) {
        event.preventDefault();
    });
}
