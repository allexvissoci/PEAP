<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


    <div id="wrapper">
        <jsp:include page="menu.jsp"/>
        <div id="page-wrapper">
            
            <div class="row">
                <div class="col-lg-12">
                    <h1 class="page-header">Usuários</h1>
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
                                        <th>Usuário</th>
                                        <th>Nome</th>
                                        <th>Status</th>
                                        <th>Tipo Usuário</th>
                                        <th>Opções</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach items="${listUsuario}" var="x">
                                        <tr>
                                            <td>${x.id}</td>
                                            <td>${x.nome}</td>
                                            <c:choose>
                                                <c:when test="${x.status}">
                                                    <td>Ativo</td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td>Inativo</td>
                                                </c:otherwise>
                                            </c:choose>
                                            <td>${x.tipoUsuario.nomeTipo}</td>
                                            <td>
                                                <i class="fa fa-edit edit-user" data-toggle="modal" data-target="#myModal" idusu="${x.id}"></i>
                                                <i class="fa fa-key reset-password" data-toggle="modal" data-target="#resetPasswordModal" idusu="${x.id}"></i>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            
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
                    <h4 class="modal-title" id="myModalLabel">Novo Usuário</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idUsuarioHide' value='0'></input>
                            <div class="form-group has-feedback">
                                <label for="nome">Nome: <span style='color:red'>*</span></label>
                                <input type="text" class="form-control" name="nome" id="nome">
                            </div>
                            <div class='row password-row'>
                                <div class='col-xs-6 col-sm-6 col-md-6'>
                                    <div class="form-group senha has-feedback">
                                        <label for="senha">Senha: <span style='color:red'>*</span></label>
                                        <input type="password" class="form-control" id="senha" name="senha">
                                    </div>
                                </div>
                                <div class='col-xs-6 col-sm-6 col-md-6'>
                                    <div class="form-group conf_senha has-feedback">
                                        <label for="conf_senha">Confirma Senha: <span style='color:red'>*</span></label>
                                        <input type="password" class="form-control" id="conf_senha" >
                                    </div>
                                <label id="resp_senha"></label>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="tipoUsuario">Tipo Usuário: <span style='color:red'>*</span></label>
                                <select class="form-control tipoUsuario" id="tipoUsuario" name="tipoUsuario">
                                </select>
                            </div>
                            <div class='form-group has-feedback'>
                                <div class="checkbox checkstatus">
                                    <label><input type="checkbox" checked name="status" id="statuscheck">Ativo</label>
                                </div>
                            </div>

                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-usuario">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->
    
    <!-- Modal -->
    <div class="modal fade" id="resetPasswordModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                    <h4 class="modal-title" id="myModalLabel">Resetar Senha</h4>
                </div>
                <div class="modal-body">
                    <!--<div class="row">-->
                        <form action='' method='post'>
                            <input type='text' class="hidden" id='idResetPassword' value='0'></input>
                            
                            <div class='row'>
                                <div class='col-xs-6 col-sm-6 col-md-6'>
                                    <div class="form-group senha has-feedback">
                                        <label for="senha">Senha: <span style='color:red'>*</span></label>
                                        <input type="password" class="form-control" id="senha" name="senha">
                                    </div>
                                </div>
                                <div class='col-xs-6 col-sm-6 col-md-6'>
                                    <div class="form-group conf_senha has-feedback">
                                        <label for="conf_senha">Confirma Senha: <span style='color:red'>*</span></label>
                                        <input type="password" class="form-control" id="conf_senha" >
                                    </div>
                                <label id="resp_senha"></label>
                                </div>
                            </div>

                        </form>

                    <!--</div>-->
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-default" data-dismiss="modal">Cancelar</button>
                    <button type="button" class="btn btn-primary btn-save-reset-password">Salvar</button>
                </div>
            </div>
            <!-- /.modal-content -->
        </div>
        <!-- /.modal-dialog -->
    </div>
    <!-- /.modal -->

<script type="text/javascript">
    
    $(document).ready(function(){   
        
        function getTipoUsuario(){
            $.ajax({
                type: "POST",
                url: "ProcessaUsuario",
                data:{
                    'function' : 'getTipoUsuario',
                },
                success: function (data) {

                    var tipoUsuario = $.parseJSON(data);
                    var varhtml = "<option value='0'></option>";
                    for (var i = 0; i < tipoUsuario.length; i++) {
                        varhtml += '<option value="'+tipoUsuario[i].id+'">'+tipoUsuario[i].nomeTipo+'</option>';
                    }
                    
                    $('.tipoUsuario').html($(varhtml));
                }
            });
        }
        
        $('.btn-novo').on('click',function(){
            getTipoUsuario();
        });
        
        
        $('.edit-user').on('click',function(){
            $('#myModal').find('.modal-title').html('Edição de Usuario');
            getTipoUsuario();
            $('.password-row').remove();
            var id = $(this).attr('idusu');
            $.ajax({
                type: "POST",
                url: "ProcessaUsuario",
                data:{
                    'function' : 'getDados',
                    'id' : id,
                },
                success: function (data) {
                    var usuario = $.parseJSON(data);
                    $("#idUsuarioHide").val(usuario.id);
                    $("#nome").val(usuario.nome);
                    $("#tipoUsuario").val(usuario.tipoUsuario.id);
                    if(usuario.status){
                        $('#statuscheck').prop('checked',true);
                    }else{
                        $('#statuscheck').prop('checked',false);
                    }
                }
            });
        });
    
        $('#myModal #senha,#myModal #conf_senha').on('change',function(){
            checksenha('myModal');
        });
        $('#resetPasswordModal #senha, #resetPasswordModal #conf_senha').on('change',function(){
            checksenha('resetPasswordModal');
        });
        
        function checksenha(idmodal){
            
            var senha = $.trim($("#"+idmodal+" #senha").val());
            var conf_senha = $.trim($("#"+idmodal+" #conf_senha").val());
            if(senha != "" && conf_senha != ""){
                if(senha == conf_senha){
                    $("#"+idmodal+" .senha").removeClass('has-error');
                    $("#"+idmodal+" .conf_senha").removeClass('has-error');
                    $("#"+idmodal+" .senha").addClass('has-success');
                    $("#"+idmodal+" .conf_senha").addClass('has-success');
                    return true;

                }else{
                    
                    $("#"+idmodal+" .senha").removeClass('has-success');
                    $("#"+idmodal+" .conf_senha").removeClass('has-success');
                    $("#"+idmodal+" .senha").addClass('has-error');
                    $("#"+idmodal+" .conf_senha").addClass('has-error');
                    return false;
                }
            }else{
                $("#"+idmodal+" .senha").removeClass('has-success');
                $("#"+idmodal+" .conf_senha").removeClass('has-success');
                $("#"+idmodal+" .senha").removeClass('has-error');
                $("#"+idmodal+" .conf_senha").removeClass('has-error');
                return false;
            }
        }
        
        $('.btn-save-usuario').on('click', function(){
            //verifica se tem id escondido
            var id = $("#idUsuarioHide").val();
            var nome = $("#nome").val();
            var senha = $("#senha").val();
            var conf_senha = $("#conf_senha").val();
            var tipo_usuario = $("#tipoUsuario").val();
            var status;
            if($('#statuscheck').is(":checked")){
                status = "true";
            }else{
                status = "false";
            }            
           

            if( (id && $.trim(nome)) || !id && $.trim(nome) != "" && $.trim(senha) != "" && $.trim(conf_senha) != ""){
                
                var senhachecked;
                if(id){
                    senhachecked = true;
                }else{
                    senhachecked= checksenha('myModal');
                }
                if(senhachecked){                    
                    $.ajax({
                        type: "POST",
                        url: "ProcessaUsuario",
                        data:{
                            'function' : 'insert',
                            'id' : id,
                            'nome': nome,
                            'senha' : senha,
                            'tipo_usuario': tipo_usuario,
                            'status' : status
                        },
                        success: function (data) {
                            if(typeof(data) !== "boolean"){
                                if(data > 0){
                                    alert("Seu nome de usuário é: " + data);
                                }
                            }else{
                                if(!data){
                                    alert("Dados não foram salvos!");
                                }
                            }
                            $('#myModal').modal('hide');
                            location.reload();
                        }
                    });
                    
                }else{
                    alert("Senhas não conferem !");
                }
            }else{
                alert("Preencha os campos obrigatórios");
            }
            
        });
        
        $('.reset-password').on('click',function(){
            var id = $(this).attr('idusu');
            $("#idResetPassword").val(id);
        });
        
        $('.btn-save-reset-password').on('click', function(){
            var id = $("#idResetPassword").val();
            var senha = $("#resetPasswordModal #senha").val();
            var senhachecked = checksenha('resetPasswordModal');
            if(senhachecked){
                $.ajax({
                    type: "POST",
                    url: "ProcessaUsuario",
                    data:{
                        'function' : 'resetPassword',
                        'id' : id,
                        'senha': senha
                    },
                    success: function (data) {
                        if(data){
                            $('#resetPasswordModal').modal('hide');
                            location.reload();                            
                        }else{
                            alert('não foi possível resetar a senha');
                        }
                    }
                });
            }
        });
    });
</script>