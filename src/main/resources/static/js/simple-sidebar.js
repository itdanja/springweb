
+function ($) {
    'use strict';
    $(document).on('click.bs.sidebar.data-api', '[data-toggle="sidebar"]', function (e) {
        e.preventDefault();
        $(".sidebar").toggleClass("toggled");
        $(".has-sidebar").toggleClass("toggled");
    });
}(jQuery);