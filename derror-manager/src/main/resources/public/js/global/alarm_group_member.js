$(function() {
    doLocale();
    getAuth();
    renderMemberCenters();
    validate();
    dealMemberCenterModal();
    handleFieldValidator();
    submitMemberCenter();
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
          $("#add-member-config").hide();
        }
      }
    }
  });
}

function renderMemberCenters() {
    $("#member-centers").bootstrapTable({
        url: "/group/member/load",
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
  var deleteTd = "<button operation='delete-group-member' class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog' memberId='" + row.id + "' data-lang='operation-remove'></button>";
  var updateTd = "<button operation='edit-group-member' class='btn-xs btn-warning' data-toggle='modal' id='edit-dialog' memberId='" + row.id + "' data-lang='operation-update'></button>";
  var operationTd = updateTd  + "&nbsp;" + deleteTd;
  return operationTd;
}

function bindButtons() {
    bindUpdateButtons();
    bindDeleteButtons();
}

function bindDeleteButtons() {
  $(document).off("click", "button[operation='delete-group-member']");
  $(document).on("click", "button[operation='delete-group-member']", function(event) {
    var memberId = $(event.currentTarget).attr("memberId");
    var msg = $.i18n.prop("operation-remove");
    showCloseConfirmModal(msg);
    $(document).off("click", "#confirm-btn");
    $(document).on("click", "#confirm-btn", function() {
      $.ajax({
        url: "/group/member/delete",
        type: "POST",
        data: JSON.stringify({"id" : memberId}),
        contentType: "application/json",
        dataType: "json",
        success: function(data) {
          $("#member-centers").bootstrapTable("refresh");
          $("#confirm-dialog").modal("hide");
          $(".modal-backdrop").remove();
          $("body").removeClass("modal-open");
        }
      });
    });
  });
}

function bindUpdateButtons() {
    $(document).off("click", "button[operation='edit-group-member']");
    $(document).on("click", "button[operation='edit-group-member']", function(event) {
        fillGroupConfig();
        var memberId = $(event.currentTarget).attr("memberId");
        $.ajax({
            url: "/group/member/getMember",
            type: "POST",
            data: JSON.stringify({"id" : memberId}),
            contentType: "application/json",
            dataType: "json",
            success: function(d) {
                if (d && d.data) {
                  var data = d.data;
                  if (data) {
                    $("#add-member-center").modal({backdrop: 'public', keyboard: true});
                    $("#memberId").val(data.id);
                    $("#groupId").val(data.alarmGroupId);
                    $("#userName").val(data.userName);
                    $("#phone").val(data.phone);
                    $("#mail").val(data.mail);
                  }
                }
            }
        });
    });
}

function dealMemberCenterModal() {
    $("#add-member-config").click(function() {
        fillGroupConfig();
        $("#add-member-center").modal({backdrop: 'public', keyboard: true});
        $("#memberId").val("");
    });
    $("#close-add-member-form").click(function() {
        $("#add-member-center").on("hide.bs.modal", function () {
            $("#member-center-form")[0].reset();
        });
        $("#member-center-form").data("bootstrapValidator").resetForm();
    });
}

function fillGroupConfig() {
  $.ajax({
    url: "/group/member/getGroups",
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
          $("#groupId").html(html);
        }
      }
    }
  });
}

function handleFieldValidator() {
    $("#modeName").focus(function() {
        $("#member-center-form").data("bootstrapValidator").enableFieldValidators("modeName", true);
    });
}

function submitMemberCenter() {
    $("#add-member-center-btn").on("click", function(event) {
        var bootstrapValidator = $("#member-center-form").data("bootstrapValidator");
        bootstrapValidator.validate();
        if(bootstrapValidator.isValid()) {
            var memberId = $("#memberId").val();
            var groupId = $("#groupId").val();
            var userName = $("#userName").val();
            var phone = $("#phone").val();
            var mail = $("#mail").val();
            $.ajax({
                url: "/group/member/addAndUpdate",
                type: "POST",
                data: JSON.stringify({"id": memberId, "alarmGroupId": groupId, "userName": userName, "phone": phone, "mail": mail}),
                contentType: "application/json",
                dataType: "json",
                success: function(data) {
                    if (data) {
                        $("#add-member-center").on("hide.bs.modal", function() {
                            $("#member-center-form")[0].reset();
                        });
                        $("#member-center-form").data("bootstrapValidator").resetForm();
                        $("#add-member-center").modal("hide");
                        $("#member-centers").bootstrapTable("refresh");
                        $(".modal-backdrop").remove();
                        $("body").removeClass("modal-open");
                    }
                }
            });
        }
    });
}

function validate() {
    $("#member-center-form").bootstrapValidator({
        message: "This value is not valid",
        feedbackIcons: {
            valid: "glyphicon glyphicon-ok",
            invalid: "glyphicon glyphicon-remove",
            validating: "glyphicon glyphicon-refresh"
        },
        fields: {
          userName: {
                validators: {
                    notEmpty: {
                        message: $.i18n.prop("alarm-group-member-name-not-null")
                    },
                    stringLength: {
                        max: 10,
                        message: $.i18n.prop("app-config-name-length-limit")
                    }
                }
            },
          phone: {
            validators: {
              notEmpty: {
                message: $.i18n.prop("alarm-group-member-phone-not-null")
              },
              stringLength: {
                min: 11,
                max: 20,
                message: $.i18n.prop("alarm-group-member-phone-limit")
              },
              regexp: {
                regexp: /^[0-9]+$/,
                message: $.i18n.prop("alarm-group-member-phone-not-support")
              }
            }
          },
          mail: {
            validators: {
              notEmpty: {
                message: $.i18n.prop("alarm-group-member-mail-not-null")
              },
              stringLength: {
                max: 50,
                message: $.i18n.prop("app-config-name-length-limit")
              },
              emailAddress :{
                message: $.i18n.prop("alarm-group-member-mail-not-support")
              }
            }
          }
        }
    });
    $("#member-center-form").submit(function(event) {
        event.preventDefault();
    });
}
