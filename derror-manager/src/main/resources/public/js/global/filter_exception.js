$(function() {
    doLocale();
    bindSelect();
    renderFilterCenters();
    validate();
    dealFilterModal();
    handleFieldValidator();
    submitFilterCenter();
    bindButtons();
});

function renderFilterCenters() {
    $("#add-filter-config").hide();
    $("#exception-centers").bootstrapTable({
        url: "/filter/exception/load",
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
    url: "/filter/exception/load",
    query:{
      "projectId":projectId
    }
  };
  $("#add-filter-config").hide();
  $("#exception-centers").bootstrapTable("refresh", opt);
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
    $("#add-filter-config").show();

    var operationTd;
    if (row.status == 1) {
      operationTd = "<button operation='close-filter-exception' class='btn-xs btn-info' exceptionId='" + row.id + "' status='0' data-lang='status-close'></button>";
    } else {
      operationTd = "<button operation='close-filter-exception' class='btn-xs btn-info' exceptionId='" + row.id + "' status='1' data-lang='status-opened'></button>";
    }
    var deleteTd = "<button operation='delete-filter-exception' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' exceptionId='" + row.id + "' data-lang='operation-remove'></button>";
    var updateTd = "<button operation='edit-filter-exception' class='btn-xs btn-warning' data-toggle='modal' id='edit-dialog' exceptionId='" + row.id + "' data-lang='operation-update'></button>";
    operationTd = operationTd + "&nbsp;" + deleteTd  + "&nbsp;" + updateTd;
    return operationTd;
}

function bindButtons() {
    bindUpdateButtons();
    bindOpenButtons();
    bindDeleteButtons();
}

function bindDeleteButtons() {
  $(document).off("click", "button[operation='delete-filter-exception']");
  $(document).on("click", "button[operation='delete-filter-exception']", function(event) {
    var exceptionId = $(event.currentTarget).attr("exceptionId");
    var msg = $.i18n.prop("operation-remove");
    showCloseConfirmModal(msg);
    $(document).off("click", "#confirm-btn");
    $(document).on("click", "#confirm-btn", function() {
      $.ajax({
        url: "/filter/exception/delete",
        type: "POST",
        data: JSON.stringify({"id" : exceptionId}),
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          $("#exception-centers").bootstrapTable("refresh");
          $("#confirm-dialog").modal("hide");
          $(".modal-backdrop").remove();
          $("body").removeClass("modal-open");
        }
      });
    });
  });
}

function bindOpenButtons() {
  $(document).off("click", "button[operation='close-filter-exception']");
  $(document).on("click", "button[operation='close-filter-exception']", function(event) {
    var status = $(event.currentTarget).attr("status");
    var msg = $.i18n.prop("confirm-to-opened");
    if (status == 1) {
      msg = $.i18n.prop("confirm-to-closed");
    }
    showCloseConfirmModal(msg);
    var id = $(event.currentTarget).attr("exceptionId");
    $(document).off("click", "#confirm-btn");
    $(document).on("click", "#confirm-btn", function() {
      $.ajax({
        url: "/filter/exception/changeStatus",
        type: "POST",
        data: JSON.stringify({"id" : id, "status" : status}),
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          $("#confirm-dialog").modal("hide");
          $(".modal-backdrop").remove();
          $("body").removeClass("modal-open");
          $("#exception-centers").bootstrapTable("refresh");
        }
      });
    });
  });
}

function bindUpdateButtons() {
    $(document).off("click", "button[operation='edit-filter-exception']");
    $(document).on("click", "button[operation='edit-filter-exception']", function(event) {
        fillAppConfig();
        var exceptionId = $(event.currentTarget).attr("exceptionId");
        $.ajax({
            url: "/filter/exception/getException",
            type: "POST",
            data: JSON.stringify({"id" : exceptionId}),
            contentType: "application/json",
            dataType: "json",
            success: function(d) {
                if (d && d.data) {
                  var data = d.data;
                  if (data) {
                    $("#add-filter-center").modal({backdrop: 'public', keyboard: true});
                    $("#exceptionId").val(data.id);
                    $("#className").val(data.className);
                    $("#appId").val(data.appId);
                    $("#status").val(data.status);
                    $("#remark").val(data.remark);
                  }
                }
            }
        });
    });
}

function dealFilterModal() {
    $("#add-filter-config").click(function() {
       fillAppConfig();
       $("#add-filter-center").modal({backdrop: 'public', keyboard: true});
       $("#exceptionId").val("");
    });
    $("#close-add-filter-form").click(function() {
      $("#add-filter-center").on("hide.bs.modal", function () {
        $("#filter-center-form")[0].reset();
      });
      $("#filter-center-form").data("bootstrapValidator").resetForm();
    });
}

function fillAppConfig() {
  $.ajax({
    url: "/filter/exception/getApps",
    type: "GET",
    dataType: "json",
    async: false,
    success: function(d) {
      if (d && d.data) {
        var data = d.data;
        if (data) {
          var msg = $.i18n.prop("filter-exception-app-select");
          var html = "<option value='0'>" + msg +"</option>";
          for(var i=0;i<data.length;i++){
            html += "<option value="+data[i].id+">"+data[i].appName+"</option>"
          }
          $("#appId").html(html);
        }
      }
    }
  });
}

function handleFieldValidator() {
    $("#className").focus(function() {
        $("#filter-center-form").data("bootstrapValidator").enableFieldValidators("className", true);
    });
}

function submitFilterCenter() {
    $("#add-filter-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#filter-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var exceptionId = $("#exceptionId").val();
            var className = $("#className").val();
            var appId = $("#appId").val();
            var status = $("#status").val();
            var remark = $("#remark").val();
          $.ajax({
                url: "/filter/exception/addAndUpdate",
                type: "POST",
                data: JSON.stringify({"id": exceptionId, "className": className, "appId": appId, "status": status,"remark":remark}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data && data.code == 0) {
                        $("#add-filter-center").on("hide.bs.modal", function() {
                            $("#filter-center-form")[0].reset();
                        });
                        $("#filter-center-form").data("bootstrapValidator").resetForm();
                        $("#add-filter-center").modal("hide");
                        reload();
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                    }

                    if (data && data.code == 1) {
                      showFailureDialog($.i18n.prop(data.msg))
                    }
                }
            });
        }
    });
}

function validate() {
    $("#filter-center-form").bootstrapValidator({
        message: "This value is not valid",
        excluded : [':disabled'],
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
          className: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("filter-exception-name-not-support")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("app-config-name-length-limit")
                    }
                }
            }
        }
    });
    $("#filter-center-form").submit(function(event) {
        event.preventDefault();
    });
}
