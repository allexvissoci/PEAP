<!DOCTYPE html>
<html lang="en">
<body>

    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <a href="${pageContext.request.contextPath}/ProcessaPost" class="carregaFeed"></a>
    </div>
    <!-- /#wrapper -->

</body>
</html>
<script type="text/javascript">
    $(document).ready(function(){
        var href =$('.carregaFeed').attr('href');
        window.location.href = href;
    });
</script>