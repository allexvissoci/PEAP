<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<body>

    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            <a href="${pageContext.request.contextPath}/ProcessaPost" class="carregaFeed"></a>
            <!--<div class="row">-->
                <div class="col-lg-12">
                    <h1 class="page-header"></h1>
                </div>
            <!--</div>-->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div style="height:55px;" class="panel-heading">
                            <i class="fa fa-clock-o fa-fw"></i> Avisos
                            <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                <button type="button" class="btn btn-primary btn-novo" data-toggle="modal" data-target="#myModal" style="float:right">Novo</button>
                            </c:if>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <ul class="timeline">
                                <c:set var="inverted" scope="session" value=""/>
                                <c:forEach items="${listaPost}" var="x">
                                    <li class="<c:out value="${inverted}"/>">
<!--                                        <div class="timeline-badge"><i class="fa fa-check"></i>
                                        </div>-->
                                        <div class="timeline-panel">
                                            <div class="timeline-heading">
                                                <h4 class="timeline-title">${x.titulo}</h4>
                                                <p><small class="text-muted"><i class="fa fa-clock-o"></i> <fmt:formatDate value="${x.dataCriacao}" pattern="dd-MM-yyyy HH:mm:ss" /> - ${x.usuario.nome}</small>
                                                </p>
                                            </div>
                                            <div class="timeline-body">
                                                <p>${x.texto}</p>
                                                <c:if test="${sessionScope.tipoUsuario eq 'Administrador'}">
                                                    <hr>
                                                    <div class="btn-group">
                                                        <button type="button" class="btn btn-primary btn-sm dropdown-toggle" data-toggle="dropdown">
                                                            <i class="fa fa-gear"></i> <span class="caret"></span>
                                                        </button>
                                                        <ul class="dropdown-menu" role="menu">
                                                            <li>
                                                                <a href="#" idpost="${x.id}" class="edit-post" data-toggle="modal" data-target="#myModal"><i style="font-size: 20px; color: #eea236;" class="fa fa-edit"></i> Editar</a>
                                                            </li>
                                                            <li>
                                                                <a href="#" idpost="${x.id}" class="delete-post"><i style="font-size: 20px; color: #d9534f;" class="glyphicon glyphicon-remove-circle"></i> Excluir</a>
                                                            </li>
                                                        </ul>
                                                    </div>
                                                </c:if>    
                                            </div>
                                        </div>
                                    </li>
                                    <c:choose>
                                        <c:when test="${inverted eq 'timeline-inverted'}">
                                            <c:set var="inverted" scope="session" value=""/>
                                        </c:when> 
                                        <c:otherwise>
                                            <c:set var="inverted" scope="session" value="timeline-inverted"/>
                                        </c:otherwise>   
                                    </c:choose>
                                </c:forEach>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12" -->
            </div>
        </div>
        <!-- /#page-wrapper -->
    </div>
    <!-- /#wrapper -->
    
    <!-- Modal -->
    <div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Novo Aviso</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idPostHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="titulo">Titulo: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="titulo" id="titulo">
                            </div>
                            <div class="form-group has-feedback">
                                <label for="texto">Texto: <span style='color:red'>*</span></label>
                                <textarea rows="6" class="form-control" name="texto" id="texto"></textarea>
                            </div>
                        </form>
                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-post">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

</body>
</html>
<script type="text/javascript">
    $(document).ready(function(){
        
        
        $('.delete-post').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idpost');
                $.ajax({
                    type: "POST",
                    url: "ProcessaPost",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       location.reload();
                    }
                });
            }else{
                return false;
            }
        });                
                
        $('.edit-post').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Post');
            var id = $(this).attr('idpost');
            $.ajax({
                type: "POST",
                url: "ProcessaPost",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var post = $.parseJSON(data);
                    $("#idPostHide").val(post.id);
                    $("#titulo").val(post.titulo);
                    $("#texto").val(post.texto);
                }
            });
        });
    
        
        $('.btn-save-post').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idPostHide").val();
            var titulo = $("#titulo").val();
            var texto = $("#texto").val();
            

            if($.trim(titulo) && $.trim(texto)){
                                
                $.ajax({
                    type: "POST",
                    url: "ProcessaPost",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'titulo': titulo,
                        'texto': texto,
                    },
                    success: function (data) {
                        
                        if(!data){
                            alert("Dados não foram salvos!");
                        }
                        
                        $('#myModal').modal('hide');
                        location.reload();
                    }
                });

            }else{
                alert("Preencha os campos obrigatórios");
            }
            
        });
    });
</script>