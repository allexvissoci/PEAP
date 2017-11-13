<%-- 
    Document   : matriculaDisciplina.jsp
    Created on : 15/09/2017, 20:29:23
    Author     : allex
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<div id="wrapper">
    <jsp:include page="menu.jsp"/>
    <div id="page-wrapper">

        <div class="row">
            <div class="col-lg-12">
                <h1 class="page-header">Matrícula Disciplina <i style="font-size: 28px;color: #337ab7;" class="fa fa-plus-circle btn-novo-matricula" data-toggle="modal" data-target="#myModal"></i></h1>

            </div>
            <!-- /.col-lg-12 -->
        </div>
        <!-- /.row -->
        <div class="row">
            <div class="col-lg-12">

                <div class="panel-body">
                    <table width="100%" class="table table-striped table-bordered table-hover" id="dataTables-example">
                        <thead>
                            <tr>
                                <th>Matrícula</th>
                                <th>Curso</th>
                                <th>Disciplina</th>
                                <th>Usuario</th>
                                <th>Opções</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${matriculasDisciplinas}" var="matriculaDisciplina">
                                <tr class="trMatricula" idMatricula="${matriculaDisciplina.id}">
                                    <td>${matriculaDisciplina.id}</td>
                                    <td>${matriculaDisciplina.disciplina.curso.nome}</td>
                                    <td>${matriculaDisciplina.disciplina.nome}</td>
                                    <td>${matriculaDisciplina.usuario.nome}</td>
                                    <td>
                                        <i style="font-size: 20px; color: #eea236;" idmatricula="${matriculaDisciplina.id}" class="fa fa-edit edit-matricula" data-toggle="modal" data-target="#myModal"></i>
                                        <i style="font-size: 20px; color: #d9534f;" idmatricula="${matriculaDisciplina.id}" class="glyphicon glyphicon-remove-circle delete-matricula"></i>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <!-- /.panel-body -->

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
                <h4 class="modal-title" id="myModalLabel">Nova Matrícula</h4>
            </div>
            <div class="modal-body">
                <!--<div class="row">-->
                    <form action='' method='post'>
                        <input type='text' class="hidden" id='idMatriculaHide' value='0'></input>
                        <div class="form-group has-feedback">
                            <label for="divSelectUsuario">Usuário: <span style='color:red'>*</span></label>
                            <select class="form-control divSelectUsuario" name="divSelectUsuario"></select>
                        </div>
                        <div class="form-group has-feedback">
                            <label for="divSelectCurso">Curso: <span style='color:red'>*</span></label>
                            <select class="form-control divSelectCurso" name="divSelectCurso"></select>
                        </div>
                        <div class="form-group has-feedback">
                            <label for="divSelectDisciplina">Disciplina: <span style='color:red'>*</span></label>
                            <select class="form-control divSelectDisciplina" name="divSelectDisciplina"></select>
                        </div>
                    </form>
                <!--</div>-->
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                <button type="button" class="btn btn-primary btn-save-matricula">Salvar</button>
            </div>
        </div>
        <!-- /.modal-content -->
    </div>
    <!-- /.modal-dialog -->
</div>
<!-- /.modal -->

<script type="text/javascript">
    
    $(document).ready(function(){ 
        
        
        $('.edit-matricula').on("click",function(){
            $('#myModal').find('.modal-title').html('Edição de Matrícula');
            getUsuarios();
            var id = $(this).attr('idmatricula');
            $.ajax({
                type: "POST",
                url: "ProcessaMatricula",
                data:{
                    'function' : 'getDadosMatriculaDisciplina',
                    'id' : id,
                },
                success: function (data) {
                    var matricula = $.parseJSON(data);
                    $("#idMatriculaHide").val(matricula.id);
                    setTimeout(function(){
                        $(".divSelectUsuario").val(matricula.usuario.id);
                        getCursos();
                        setTimeout(function(){
                            $(".divSelectCurso").val(matricula.disciplina.curso.id);
                            getDisciplinas(true);
                            setTimeout(function(){
                                $(".divSelectDisciplina").val(matricula.disciplina.id);
                            },20);
                        },20);
                    },50);
                    
                }
            });
        });
        
        $('.delete-matricula').on('click',function(){
            var r = confirm("Tem certeza que deseja excluir ?");
            if (r == true) {
                var id = $(this).attr('idmatricula');
                $.ajax({
                    type: "POST",
                    url: "ProcessaMatricula",
                    data:{
                        'function' : 'deleteMatriculaDisciplina',
                        'id' : id,
                    },
                    success: function (data) {
                       alert('Matricula deletada com sucesso');
                       location.reload();
                    }
                });
            }else{
                return false;
            }
            
        });
        
        $('.btn-save-matricula').on('click', function(){
           var id = $("#idMatriculaHide").val();
           var usuarioId = $('.divSelectUsuario').val();
           var disciplinaId = $(".divSelectDisciplina").val();
           
           //// ajax to save
            $.ajax({
                type: "POST",
                url: "ProcessaMatricula",
                data:{
                    'function' : 'insertMatriculaDisciplina',
                    'id':id,
                    'usuarioId': usuarioId,
                    'disciplinaId' : disciplinaId,
                },
                success: function (data) {
                    location.reload();
                }
            });
           
        });
    
        function getDisciplinas(edit){
            var cursoId = $('.divSelectCurso').val();
            if(edit){
                var usuarioId = 0;
            }else{
                var usuarioId = $(".divSelectUsuario").val();
            }
            $.ajax({
                type: "POST",
                url: "ProcessaDisciplina",
                data:{
                    'function' : 'toSelect',
                    'cursoId'  : cursoId,
                    'usuarioId': usuarioId
                },
                success: function (data) {
                    console.log(data);
                    var disciplina = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < disciplina.length; i++){
                        option += "<option value='"+disciplina[i].id+"'>"+disciplina[i].nome+"</option>";
                    }
                    $(".divSelectDisciplina").html(option);
                }
            });
        }
        
        function getUsuarios(){
            $.ajax({
                type: "POST",
                url: "ProcessaUsuario",
                data:{
                    'function' : 'toSelect',
                },
                success: function (data) {
                    var usuario = $.parseJSON(data);
                    var option = "<option value='0'> </option>";
                    for(var i = 0; i < usuario.length; i++){
                        option += "<option value='"+usuario[i].id+"'>"+usuario[i].nome+"</option>";
                    }
                    $(".divSelectUsuario").html(option);
                }
            });
        }
        $(".divSelectUsuario").on('change', function(){
            getCursos();
        });
        function getCursos(){
            $.ajax({
                type: "POST",
                url: "ProcessaCurso",
                data:{
                    'function' : 'getCursosByUsuariotoSelect',
                    'usuarioId': $(".divSelectUsuario").val()
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
        $(".divSelectCurso").on('change',function(){
           getDisciplinas(false);
        });
        
        $('.btn-novo-matricula').on('click', function(){
           getUsuarios();
        });
    });
</script>
