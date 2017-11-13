<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Disciplinas</h1>
                </div>
                <!-- /.col-lg-12 -->
            </div>
            <!-- /.row -->
            <div class="row">
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            <button type="button" class="btn btn-primary btn-novo" data-toggle="modal" data-target="#myModal" idfun="${x.id}">Novo</button>
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                                <thead>
                                    <tr>
                                        <th>Disciplina</th>
                                        <th>Curso</th>
                                        <th>Opções</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listaDisciplina}" var="x">
                                        <tr>
                                            <td class="trDisciplina" iddisciplina="${x.id}" >${x.nome}</td>
                                            <td class="trDisciplina" iddisciplina="${x.id}" >${x.curso.nome}</td>
                                            <td>
                                                <i style="font-size: 20px; color: #eea236;" iddisciplina="${x.id}" class="fa fa-edit edit-disciplina" data-toggle="modal" data-target="#myModal"></i>
                                                <i style="font-size: 20px; color: #d9534f;" iddisciplina="${x.id}" class="glyphicon glyphicon-remove-circle delete-disciplina"></i>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <a class="linkDisciplinaDetail" href="${pageContext.request.contextPath}/ProcessaDisciplina"></a>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-lg-12 -->
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
                    <h4 class="modal-title" id="myModalLabel">Nova Disciplina</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idDisciplinaHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="nome">Disciplina: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="nome" id="nome">
                            </div>
                            <div class="form-group has-feedback">
                                <label for="divSelectCurso">Curso: <span style='color:red'>*</span></label>
                                <select class="form-control divSelectCurso" name="divSelectCurso"></select>
                            </div>
                        </form>
                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-disciplina">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    


<script type="text/javascript">
    
    $(document).ready(function(){
        
        $('.trDisciplina').on('click',function(){
            var id = $(this).attr('iddisciplina');
            var href =$('.linkDisciplinaDetail').attr('href');
            window.location.href = href+'?function=disciplinaDetail&id='+id;
        });
        
        function getCursos(){
            $.ajax({
                type: "POST",
                url: "ProcessaCurso",
                data:{
                    'function' : 'toSelect',
                    'usuarioId' : 0
                },
                success: function (data) {
                    var curso = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < curso.length; i++){
                        option += "<option value='"+curso[i].id+"'>"+curso[i].nome+"</option>";
                    }
                    $(".divSelectCurso").html(option);
                }
            });
        }
        
        $('.btn-novo').on('click', function(){
           getCursos();
        });
                
                
        $('.delete-disciplina').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('iddisciplina');
                $.ajax({
                    type: "POST",
                    url: "ProcessaDisciplina",
                    data:{
                        'function' : 'delete',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('Disciplina deletada com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
            
        });                
                
        $('.edit-disciplina').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Disciplina');
            getCursos();
            var id = $(this).attr('iddisciplina');
            $.ajax({
                type: "POST",
                url: "ProcessaDisciplina",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var disciplina = $.parseJSON(data);
                    $("#idDisciplinaHide").val(disciplina.id);
                    $("#nome").val(disciplina.nome);
                    setTimeout(function(){
                        $(".divSelectCurso").val(disciplina.curso.id);
                    },50);
                }
            });
        });
    
        
        $('.btn-save-disciplina').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idDisciplinaHide").val();
            var nome = $("#nome").val();
            var cursoId = $(".divSelectCurso").val();

            if($.trim(nome) && cursoId > 0){
                                
                $.ajax({
                    type: "POST",
                    url: "ProcessaDisciplina",
                    data:{
                        'function' : 'insert',
                        'id' : id,
                        'nome': nome,
                        'cursoId': cursoId,
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