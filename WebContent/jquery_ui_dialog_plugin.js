(function($) {
    var _create = $.ui.dialog.prototype._create;
    $.ui.dialog.prototype._create = function() {
        var self = this;
        _create.apply(this, arguments);
        var textareaHasFocus = false;
        var saveLeft, saveTop;
        if (self.options.sticky)
            self.uiDialog.css('position', 'fixed');
        this.uiDialog
            .find(':input').bind('focus', function(e) {
                textareaHasFocus = $(this).is('textarea');
            })
            .end()
            .bind('keypress', function(e) {
                if (e.which == 13 && !textareaHasFocus && self.options.defaultButton != "") {
                    var $obj = $(this).find(":button:contains(" + self.options.defaultButton + ")");
                    var isJQuery = $obj instanceof jQuery;
                    if (isJQuery) {
                        $obj.click();
                        e.preventDefault();
                    };
                };
            })
            .bind('dialogopen', function(event, ui) {
                self.uiDialog.find(':button').removeClass('ui-state-focus');
                if (self.options.defaultButton != '')
                    self.uiDialog.find(':button:contains(' + self.options.defaultButton + ')').addClass('ui-state-focus');
            })
            .bind('dragstop', function(event, ui) {
                if (self.options.sticky) {
                    saveLeft = ui.position.left;
                    saveTop = ui.position.top;
                }
            })
            .bind('resizestart', function(event, ui) {
                if (self.options.sticky) {
                    saveLeft = ui.position.left - $(window).scrollLeft();
                    saveTop = ui.position.top - $(window).scrollTop();
                }
            })
            .bind('resizestop', function(event, ui) {
                if (self.options.sticky)
                    self.uiDialog.css({ 'position': 'fixed', 'left': saveLeft, 'top': saveTop });
            });
    };
    $.ui.dialog.prototype.options.sticky = true;
    $.ui.dialog.prototype.options.defaultButton = '';
})(jQuery);
