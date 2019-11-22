$(function() {
    doLocale();
    getAuth();
    renderGroupCenters();
    validate();
    dealGroupModal();
    handleFieldValidator();
    submitGroupCenter();
    bindButtons();
});

/**
 * 获取权限
 */
function getAuth() {
  $.ajax({
    url: "/user/auth",
    type: "GET",
    dataType: "json",
    success: function(data) {
      if (data) {
        var roleId = data.data;
        if (roleId != 1) {
          $("#add-group-config").hide();
        }
      }
    }
  });
}

function renderGroupCenters() {
    $("#group-centers").bootstrapTable({
        url: "/group/load",
        cache: false,
        search: true,
        showRefresh: true,
        showColumns: true
    }).on("all.bs.table", function() {
        doLocale();
    });
}

function generateOperationButtons(val, row) {
  //超级管理有权限
  if (row.roleId != 1) {
    return "-";
  }
  var deleteTd = "<button operation='delete-alarm-group' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' groupId='" + row.id + "' data-lang='operation-remove'></button>";
    var updateTd = "<button operation='edit-alarm-group' class='btn-xs btn-warning' data-toggle='modal' id='edit-dialog' groupId='" + row.id + "' data-lang='operation-update'></button>";
    var operationTd = updateTd  + "&nbsp;" + deleteTd;
    return operationTd;
}

function bindButtons() {
    bindUpdateButtons();
    bindDeleteButtons();
}

function bindDeleteButtons() {
  $(document).off("click", "button[operation='delete-alarm-group']");
  $(document).on("click", "button[operation='delete-alarm-group']", function(event) {
    var groupId = $(event.currentTarget).attr("groupId");
    var msg = $.i18n.prop("operation-remove");
    showCloseConfirmModal(msg);
    $(document).off("click", "#confirm-btn");
    $(document).on("click", "#confirm-btn", function() {
      $.ajax({
        url: "/group/delete",
        type: "POST",
        data: JSON.stringify({"id" : groupId}),
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          $("#group-centers").bootstrapTable("refresh");
          $("#confirm-dialog").modal("hide");
          $(".modal-backdrop").remove();
          $("body").removeClass("modal-open");
        }
      });
    });
  });
}

function bindUpdateButtons() {
    $(document).off("click", "button[operation='edit-alarm-group']");
    $(document).on("click", "button[operation='edit-alarm-group']", function(event) {
        var groupId = $(event.currentTarget).attr("groupId");
        $.ajax({
            url: "/group/getGroup",
            type: "POST",
            data: JSON.stringify({"id" : groupId}),
            contentType: "application/json",
            dataType: "json",
            success: function(d) {
                if (d && d.data) {
                  var data = d.data;
                  if (data) {
                    $("#add-group-center").modal({backdrop: 'public', keyboard: true});
                    $("#id").val(data.id);
                    $("#name").val(data.name);
                    $("#remark").val(data.remark);
                  }
                }
            }
        });
    });
}

function dealGroupModal() {
    $("#add-group-config").click(function() {
       $("#add-group-center").modal({backdrop: 'public', keyboard: true});
       $("#id").val("");
    });
    $("#close-alarm-group-form").click(function() {
      $("#add-group-center").on("hide.bs.modal", function () {
        $("#alarm-group-form")[0].reset();
      });
      $("#alarm-group-form").data("bootstrapValidator").resetForm();
    });
}

function handleFieldValidator() {
    $("#name").focus(function() {
        $("#alarm-group-form").data("bootstrapValidator").enableFieldValidators("name", true);
    });
}

function submitGroupCenter() {
    $("#add-group-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#alarm-group-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
          var id = $("#id").val();
          var name = $("#name").val();
          var remark = $("#remark").val();
          $.ajax({
                url: "/group/addAndUpdate",
                type: "POST",
                data: JSON.stringify({"id": id, "name": name,"remark":remark}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data) {
                        $("#add-group-center").on("hide.bs.modal", function() {
                            $("#alarm-group-form")[0].reset();
                        });
                        $("#alarm-group-form").data("bootstrapValidator").resetForm();
                        $("#add-group-center").modal("hide");
                        $("#group-centers").bootstrapTable("refresh");
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                    }

                }
            });
        }
    });
}

function validate() {
    $("#alarm-group-form").bootstrapValidator({
        message: "This value is not valid",
        excluded : [':disabled'],
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
          name: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("alarm-group-name-not-null")
                    },
                    stringLength: {
                        max: 50,
                        message: $.i18n.prop("app-config-name-length-limit")
                    },
                    callback: {
                    message: $.i18n.prop("alarm-group-name-existed"),
                    callback: function() {
                      var name = $("#name").val();
                      var result = true;
                      $.ajax({
                        url: "/group/existed",
                        type: "POST",
                        data: JSON.stringify({"name": name}),
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
    $("#alarm-group-form").submit(function(event) {
        event.preventDefault();
    });
}
