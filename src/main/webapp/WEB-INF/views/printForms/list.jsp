<%-- 
    Document   : newjsp
    Created on : 31 авг. 2020 г., 10:22:42
    Author     : Kalinin Maksim
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>CICADA | Шаблоны печати</title>

        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="dist/css/AdminLTE.css" type="text/css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/css/select2.min.css"/>
        <link rel="stylesheet" href="http://ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/themes/sunny/jquery-ui.css">
        <!-- Select2 -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/bower_components/select2/dist/css/select2.min.css">
        <!-- Select2 -->

        <script src="bower_components/select2/dist/js/select2.full.min.js"></script>

        <script src="//ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js"></script>
        <script src="//ajax.aspnetcdn.com/ajax/jquery.ui/1.10.3/jquery-ui.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/select2@4.1.0-beta.1/dist/js/select2.min.js"></script>

        <script type="text/javascript">
            $(document).ready(function () {
                $('#formSelector').select2({
                    ajax: {
                        url: 'findByName',
                        
                        data: function (params) {
                            return {
                                name: params.term
                            };
                        },
                        processResults: function (data) {
                            return {
                                results: data
                            };
                        },
                    }
                });
                $('#formSelector').change(function () {

                    document.location = "form?id=" + this.options[this.selectedIndex].value;
                });
            })
        </script>
    </head>
    <body class="hold-transition skin-blue sidebar-mini">
        <div class="wrapper">
            <!-- HEADER -->
            <jsp:include page="../_header.jsp"></jsp:include>
                <!-- Left side column. contains the logo and sidebar -->
            <jsp:include page="../_leftmenu.jsp"></jsp:include>
            <!-- Content Wrapper. Contains page content -->
            <div class="content-wrapper">
                <section class="content-header">
                    <select id ="formSelector" class="form-control select2 input-sm" style="width: 100%">
                    </select>
                </section>
            </div>
        </div>
    </body>
</html>
