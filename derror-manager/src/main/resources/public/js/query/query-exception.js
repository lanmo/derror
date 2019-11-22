$(function () {
  doLocale();
  validate();
  handleFieldValidator();
  var table;
  $(".form_datetime").datetimepicker(
      {format: 'yyyy-mm-dd hh:ii', autoclose: true, todayBtn: true});
  $.ajax({
    url: "/appConfig/load/ProjectId",
    type: "GET",
    async: false,
    dataType: "json",
    success: function (d) {
      if (d && d.data) {
        var data = d.data;
        if (data) {
          var html = "";
          for (var i = 0; i < data.length; i++) {
            html += "<option value=" + data[i].id + ">" + data[i].appName
                + "</option>"
          }
          $("#app-query").html(html);
        }
      }
    },
    complete: function (XMLHttpRequest, textStatus) {
      // 通过XMLHttpRequest取得响应头，REDIRECT
      var redirect = XMLHttpRequest.getResponseHeader("REDIRECT");//若HEADER中含有REDIRECT说明后端想重定向
      if (redirect == "REDIRECT") {
        window.location.href = XMLHttpRequest.getResponseHeader("CONTEXTPATH");
      }else{
          renderRegCenters("/exception/log/query");
          $("#btn_query").click(function () {
              var app = $("#app-query").val();
              var host = $("#host-query").val();
              var start = $("#start-time").val();
              var end = $("#end-time").val();
              if (end.trim().length > 0 && start.trim().length <= 0 || start.trim().length
                  > 0 && end.trim().length <= 0) {
                  alert("开始时间或者结束时间不能为空");
                  return;
              }
              var url = "/exception/log/query?appId=" + app
                  + "&host="
                  + host + "&startTime=" + start + "&endTime=" + end;
              renderRegCenters(url);
          });
      }
    },
  });
});

function renderRegCenters(url) {
  table = $('#example').DataTable({
    destroy: true,
    searching: false,
    paging: false,
    "ajax": url,
    "columns": [
      {
        "className": 'details-control',
        "orderable": false,
        "data": null,
        "defaultContent": ''
      },
      {"data": "host"},
      {"data": "shortName"},
      {"data": "appName"},
      {"data": "createDate"}
    ],
    "order": [[1, 'asc']],
  });

  $('#example tbody').unbind("click").on('click', 'td.details-control',
      function () {
        var tr = $(this).closest('tr');
        var row = table.row(tr);
        if (row.child.isShown()) {
          row.child.hide();
          tr.removeClass('shown');
        } else {
          row.child(format(row.data())).show();
          tr.addClass('shown');
        }
      });
}

function format(d) {
  return '<table cellpadding="5" cellspacing="0" border="0" style="padding-left:50px;">'
      +
      '<tr>' +
      '<td> traceId:</td>' +
      '<td>' + d.traceId + '</td>' +
      '</tr>' +
      '<tr>' +
      '<td> 异常全类名:</td>' +
      '<td>' + d.className + '</td>' +
      '</tr>' +
      '<tr>' +
      '<td> mdc:</td>' +
      '<td>' + d.mdcValue + '</td>' +
      '</tr>' +
      '<tr>' +
      '<td> 扩展值:</td>' +
      '<td>' + d.ext + '</td>' +
      '</tr>' +
      '<tr>' +
      '<td> 环境:</td>' +
      '<td>' + d.env + '</td>' +
      '</tr>' +
      '<tr>' +
      '<td> 异常堆栈:</td>' +
      '<td>' + d.content + '</td>' +
      '</tr>' +
      '</table>';
}

function generateOperationButtons(val, row) {
  var deleteTd = "<button operation='delete-alarm-mail'"
      + " class='btn-xs btn-danger' data-toggle='modal' id='delete-dialog'"
      + " mailId='" + row.id + "' data-lang='operation-remove'></button>";
  var updateTd = "<button operation='edit-alarm-mail' class='btn-xs"
      + " btn-warning' data-toggle='modal' id='edit-dialog' mailId='" + row.id
      + "' data-lang='operation-update'></button>";
  var operationTd = updateTd + "&nbsp;" + deleteTd;
  return operationTd;
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

function handleFieldValidator() {
  $("#name").focus(function () {
    $("#mail-center-form").data("bootstrapValidator").enableFieldValidators(
        "name", true);
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








