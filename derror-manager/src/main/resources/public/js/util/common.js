$(function() {
    $("[data-toggle='tooltip']").tooltip();
});

function showDialog(msg, timeout) {
    $("#message-info").text(msg);
    $("#message-dialog").modal("show");
    if(null !== timeout) {
        setTimeout('$("#message-dialog").modal("hide")', timeout);
    }
}

function showSuccessDialog() {
    showInfoDialog($.i18n.prop("operation-succeed"));
}

function showFailDialog(msg) {
  showInfoDialog(msg);
}

function showInfoDialog(msg) {
    showDialog(msg, 2000);
}

function showFailureDialog(msg) {
    showDialog(msg, null);
}

function showCloseConfirmModal(msg) {
    $("#confirm-info").text(msg);
    $("#confirm-dialog").modal({backdrop: 'public', keyboard: true});
}

function showShutdownConfirmModal() {
    $("#confirm-info").text($.i18n.prop("confirm-to-close"));
    $("#confirm-dialog").modal({backdrop: 'public', keyboard: true});
}

function showUpdateConfirmModal() {
    $("#confirm-info").text($.i18n.prop("update-job-confirm-info"));
    $("#confirm-dialog").modal({backdrop: 'public', keyboard: true});
}

function i18n(lang) {
    jQuery.i18n.properties({
        name : 'message',
        path : '/i18n/',
        mode : 'map',
        language : lang,
        cache: true,
        encoding: 'UTF-8',
        callback : function() {
            for (var i in $.i18n.map) {
                $('[data-lang="'+i+'"]').html($.i18n.prop(i));
            }
        }
    });
}

function doLocale() {
    if ($("#content").hasClass("lang-en")) {
        i18n("en");
    } else {
        i18n("zh");
    }
}

function switchLanguage() {
    $("#lang-zh").click(function() {
        $("#content").removeClass("lang-en").addClass("lang-zh");
        doLocale();
    });
    $("#lang-en").click(function() {
        $("#content").removeClass("lang-zh").addClass("lang-en");
        doLocale();
    });
}

/**
 * 设置列表参数
 */
function bindSelect() {
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
          if (data.length <= 0) {
            var msg = $.i18n.prop("filter-exception-app-select");
            html = "<option value='0'>" + msg + "</option>";
          } else {
            for (var i = 0; i < data.length; i++) {
              html += "<option value=" + data[i].id + ">" + data[i].name + "</option>"
            }
          }
          $("#project-query").html(html);
        }
      }
    }
  });
}
