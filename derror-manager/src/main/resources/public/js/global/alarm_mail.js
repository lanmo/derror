$(function () {
  doLocale();
  getAuth();
  renderMailCenters();
  validate();
  dealMailModal();
  handleFieldValidator();
  submitMailCenter();
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
          $("#add-alarm-mail-config").hide();
        }
      }
    }
  });
}

function renderMailCenters() {
  $("#alarm-mail-centers").bootstrapTable({
    url: "/alarm/mail/query",
    cache: false,
    search: true,
    showRefresh: true,
    showColumns: true
  }).on("all.bs.table", function () {
    doLocale();
  });
}

function generateOperationButtons(val, row) {
  //超级管理有权限
  if (row.roleId != 1) {
    return "-";
  }

  var deleteTd = "<button operation='delete-alarm-mail'"
      + " class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog'"
      + " mailId='" + row.id + "' data-lang='operation-remove'></button>";
  var updateTd = "<button operation='edit-alarm-mail' class='btn-xs"
      + " btn-warning' data-toggle='modal' id='edit-dialog' mailId='" + row.id
      + "' data-lang='operation-update'></button>";
  var operationTd = updateTd + "&nbsp;" + deleteTd;
  return operationTd;
}

function bindButtons() {
  bindUpdateButtons();
  bindDeleteButtons();
}

function bindDeleteButtons() {
  $(document).off("click", "button[operation='delete-alarm-mail']");
  $(document).on("click", "button[operation='delete-alarm-mail']",
      function (event) {
        var mailId = $(event.currentTarget).attr("mailId");
        var msg = $.i18n.prop("operation-remove");
        showCloseConfirmModal(msg);
        $(document).off("click", "#confirm-btn");
        $(document).on("click", "#confirm-btn", function () {
          $.ajax({
            url: "/alarm/mail/delete",
            type: "POST",
            data: JSON.stringify({"id": mailId}),
            contentType: "application/json",
            dataType: "json",
            success: function (data) {
              $("#alarm-mail-centers").bootstrapTable("refresh");
              $("#confirm-dialog").modal("hide");
              $(".modal-backdrop").remove();
              $("body").removeClass("modal-open");
            }
          });
        });
      });
}

function bindUpdateButtons() {
  $(document).off("click", "button[operation='edit-alarm-mail']");
  $(document).on("click", "button[operation='edit-alarm-mail']",
      function (event) {
        var mailId = $(event.currentTarget).attr("mailId");
        $.ajax({
          url: "/alarm/mail/query",
          type: "POST",
          data: JSON.stringify({"id": mailId}),
          contentType: "application/json",
          dataType: "json",
          success: function (d) {
            if (d && d.data) {
              var data = d.data;
              if (data) {
                $("#add-mail-center").modal(
                    {backdrop: 'public', keyboard: true});
                $("#id").val(data.id);
                $("#host").val(data.host);
                $("#port").val(data.port);
                $("#username").val(data.username);
                $("#password").val(data.password);
              }
            }
          }
        });
      });
}

function dealMailModal() {
  $("#add-alarm-mail-config").click(function () {
    $("#add-mail-center").modal({backdrop: 'public', keyboard: true});
    $("#id").val("");
  });
  $("#close-mail-form").click(function () {
    $("#add-mail-center").on("hide.bs.modal", function () {
      $("#mail-center-form")[0].reset();
    });
    $("#mail-center-form").data("bootstrapValidator").resetForm();
  });
}

function handleFieldValidator() {
  $("#name").focus(function () {
    $("#mail-center-form").data("bootstrapValidator").enableFieldValidators(
        "name", true);
  });
}

function submitMailCenter() {
  $("#add-mail-center-btn").on("click", function (event) {
    var bootstrapValidator = $("#mail-center-form").data("bootstrapValidator");
    bootstrapValidator.validate();
    if (bootstrapValidator.isValid()) {
      var id = $("#id").val();
      var host = $("#host").val();
      var port = $("#port").val();
      var username = $("#username").val();
      var password = $("#password").val();
      $.ajax({
        url: "/alarm/mail/addAndUpdate",
        type: "POST",
        data: JSON.stringify({"id": id, "host": host, "port": port, "username": username, "password": password}),
        contentType: "application/json",
        dataType: "json",
        success: function (data) {
          if (data) {
            $("#add-mail-center").on("hide.bs.modal", function () {
              $("#mail-center-form")[0].reset();
            });
            $("#mail-center-form").data("bootstrapValidator").resetForm();
            $("#add-mail-center").modal("hide");
            $("#alarm-mail-centers").bootstrapTable("refresh");
            $(".modal-backdrop").remove();
            $("body").removeClass("modal-open");
          }

        }
      });
    }
  });
}

function validate() {
  $("#mail-center-form").bootstrapValidator({
    message: "This value is not valid",
    excluded: [':disabled'],
    feedbackIcons: {
      valid: "glyphicon glyphicon-ok",
      invalid: "glyphicon glyphicon-remove",
      validating: "glyphicon glyphicon-refresh"
    },
    fields: {
      username: {
        validators: {
          notEmpty: {
            message: $.i18n.prop("alarm-group-name-not-null")
          },
          stringLength: {
            max: 50,
            message: $.i18n.prop("app-config-name-length-limit")
          }
        }
      }
    }
  });
  $("#mail-center-form").submit(function (event) {
    event.preventDefault();
  });
}
