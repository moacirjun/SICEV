/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;
import sicev.DAO.DaoUsuario;
import sicev.model.ModelUsuario;
import sicev.view.SICEV;

/**
 *
 * @author Moacir
 */
public class UsuarioController implements Initializable{

    @FXML private GridPane paneCampos;
    @FXML private VBox tableContainer;
    @FXML private TableView<ModelUsuario> tabelaUsuarios;
    @FXML private ToggleButton btnNovo;
    @FXML private ToggleButton btnEditar;
    @FXML private TextField edtPesquisar;
    @FXML private Button btnRemoveFiltro;
    @FXML private Button btnExcluir;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    
    @FXML private TextField edtCodigo;
    @FXML private TextField edtNome;
    @FXML private TextField edtLogin;
    @FXML private TextField edtSenha;
    
    private final DaoUsuario daoUsuario = new DaoUsuario();
    private final ModelUsuario modelUsuario = new ModelUsuario();
    
    private final BooleanProperty tableSelectionIsNull = new SimpleBooleanProperty();
    private final BooleanProperty btnNovoSelected = new SimpleBooleanProperty();
    private final BooleanProperty btnEditarSelected = new SimpleBooleanProperty();
    
    private ObservableList<ModelUsuario> listaUsuarios;
    private FilteredList<ModelUsuario> filteredListUsuarios;
    
    private SICEV application;
    
    /**
     * Expressão lambda que implementa o bind dos campos com a linha selecionada
     * da tabela
     */
    final ChangeListener<ModelUsuario> TableListener = 
        (ObservableValue<? extends ModelUsuario> observable, 
                ModelUsuario oldValue, ModelUsuario newValue) -> {
            
            setModelUsuarioBinds(newValue);
        };
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Carrega a tabela
        carregarProdutos();
        
        tableSelectionIsNull.bind( tabelaUsuarios.getSelectionModel().selectedItemProperty().isNull() );
        btnNovoSelected.bind( btnNovo.selectedProperty() );
        btnEditarSelected.bind( btnEditar.selectedProperty() );
        
        //Regra dos estados dos botões
        btnNovo.disableProperty().bind(btnEditarSelected);
        btnEditar.disableProperty().bind(tableSelectionIsNull.or(btnNovoSelected));
        btnExcluir.disableProperty().bind(tableSelectionIsNull.
                or(btnNovoSelected).or(btnEditarSelected));
        
        btnCancelar.visibleProperty().bind( btnNovoSelected.or(btnEditarSelected) );
        btnSalvar.visibleProperty().bind(btnNovoSelected.or(btnEditarSelected) );
        
        //Regra dos estados dos containers da tela
        paneCampos.disableProperty().bind(
                ( btnNovoSelected.not() ).and(
                ( btnEditarSelected.not() )));
        tableContainer.disableProperty().bind(btnNovoSelected.or( btnEditarSelected ));
        
        tableSelectionIsNull.addListener( //Limpa os campos quando nenhuma linha 
                                          //da tabela estiver selecionada  
                (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                    if (newValue) {
                        setModelUsuarioBinds(new ModelUsuario());
                    }
                });
        
        
        //Cria um bind bidirecional entre os campos e o objeto "modelUsuario"
        edtCodigo.textProperty().bindBidirectional(
                modelUsuario.idProperty(), new NumberStringConverter());
        
        edtNome.textProperty().bindBidirectional(modelUsuario.nomeProperty());
        edtLogin.textProperty().bindBidirectional(modelUsuario.loginProperty());
        edtSenha.textProperty().bindBidirectional(modelUsuario.senhaProperty());
        
        
        //Bind automático dos campos ao selecionar uma linha na tabela
        tabelaUsuarios.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
        
        //Set the filter Predicate whenever the filter changes.
        edtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredListUsuarios.setPredicate(usuario -> {
                // If filter text is empty, display all produtos.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare name and cidade of every cliente with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (usuario.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (usuario.getLogin().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        //Configura a tela
        setStatusTelaExibir();
    }
    
    public void setApp(SICEV application) {
        this.application = application;
    }
    
    public void carregarProdutos() {
        listaUsuarios = FXCollections.observableList(getAllUusuarios());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredListUsuarios = new FilteredList<>(listaUsuarios, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelUsuario> sortedData = new SortedList<>(filteredListUsuarios);

        // 3. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tabelaUsuarios.comparatorProperty());    
        
        //Remove o changeListener da tabela enquanto atualiza seus items
        tabelaUsuarios.getSelectionModel().selectedItemProperty().
                removeListener(TableListener);
        
        // 4. Add sorted (and filtered) data to the table.
        tabelaUsuarios.setItems(sortedData);
        
        tabelaUsuarios.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }
    
    private void setModelUsuarioBinds (ModelUsuario currentProd) {
        if ( currentProd != null ) {
            modelUsuario.idProperty().bind(currentProd.idProperty());
            modelUsuario.nomeProperty().bind(currentProd.nomeProperty());
            modelUsuario.loginProperty().bind(currentProd.loginProperty());
            modelUsuario.senhaProperty().bind(currentProd.senhaProperty());
        } 
        else {
            modelUsuario.idProperty().unbind();
            modelUsuario.nomeProperty().unbind();
            modelUsuario.loginProperty().unbind();
            modelUsuario.senhaProperty().unbind();
        }
    }
    
    /**
     * <p>seleciona a primeira linha na tabela</p>
     */
    private void setStatusTelaExibir () {
        setStatusTelaExibir(-1);
    }
    
    /**
     * <p>para selecionar a última linha passe -2 como parâmetro</p>
     * <p>para selecionar a primeira linha passe -1 como parâmetro</p>
     * @param tableRowIndex 
     */
    private void setStatusTelaExibir (int tableRowIndex) {
        btnNovo.setSelected(false);
        btnEditar.setSelected(false);
        switch (tableRowIndex) {
            case -2:
                tabelaUsuarios.getSelectionModel().selectLast();
                break;
            case -1:
                tabelaUsuarios.getSelectionModel().selectFirst();
                break;
            default:
                tabelaUsuarios.getSelectionModel().select(tableRowIndex);
                break;
        }
        
        tabelaUsuarios.requestFocus();
        
        /**
         * faz um bind com a linha atual da tabela, caso o evento ChangeLister
         * da tabela não seja invocado
         */        
        setModelUsuarioBinds( tabelaUsuarios.getSelectionModel().getSelectedItem() );

        //Devolve os eventos padrões dos botões Editar e Novo
        btnNovo.setOnAction((e) -> {handleBtnNovoAction(e);});
        btnEditar.setOnAction((e) -> {handleBtEditarAction(e);});
    }
    
    /* ====================== Métodos da classe Dao ========================= */
    private int salvarUsuario (ModelUsuario pModelUsuario) {
        return  this.daoUsuario.salvarUsuarioDao(pModelUsuario);
    }
    
    private boolean excluirUsuario (int idUsuario) {
        return this.daoUsuario.excluirUsuarioDao(idUsuario);
    }
    
    private boolean atualizarUsuario (ModelUsuario pModelUsuario) {
        return this.daoUsuario.atualizarUsuarioDao(pModelUsuario);
    }
    
    private ModelUsuario getUsuarioById (int idUsuario) {
        return this.daoUsuario.getUsuarioByIdDao(idUsuario);
    }
    
    private ArrayList<ModelUsuario> getAllUusuarios() {
        return this.daoUsuario.getAllUsuariosDao();
    }
    /* ====================================================================== */
    
    /* ==================== Eventos da Interface ============================ */
    @FXML
    private void handleBtnNovoAction (ActionEvent event) {        
        
        //passa um objeto ModelUsuario vazio para limpar os campos
        setModelUsuarioBinds( new ModelUsuario() );
        
        //Retira o bind do modelUsuario com esse ModelUSuario vazio passado anteriormente
        setModelUsuarioBinds(null);
        
        //Comfiguração dos componentes da tela
        edtNome.requestFocus();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnNovo.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtEditarAction (ActionEvent event) {
        //Retira o bind do modelUsuario para que seja possível a alteração
        setModelUsuarioBinds(null);
        
        edtNome.requestFocus();
        edtNome.selectEnd();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnEditar.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtnExcluirAction (ActionEvent event) {
        
        if (confirmAlert("Confirmar a exclusão do cliente?") == ButtonBar.ButtonData.YES) {
            
            int newIndex = 0;
            
            if ( excluirUsuario(tabelaUsuarios.getSelectionModel().
                    getSelectedItem().getId()) ) {
                
                //Guarda o index da linha anterior ou posterior à linha excluída
                int curIndex = tabelaUsuarios.getSelectionModel().getSelectedIndex();
                if ( curIndex ==  0) {
                    tabelaUsuarios.getSelectionModel().selectNext();
                }
                else {
                    tabelaUsuarios.getSelectionModel().selectPrevious();
                }
                newIndex = tabelaUsuarios.getSelectionModel().getSelectedIndex();
                
                //Recarrega os produtos do banco
                carregarProdutos();
                
                showAlert(Alert.AlertType.INFORMATION, "Cliente excuído com sucesso!");
            }
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível excluir o cliente!");
            }
            
            //Deixa a tela no mode de exibição
            setStatusTelaExibir(newIndex);
        }
    }
    
    @FXML
    private void handleBtnSalvarAction (ActionEvent event) {
        
        if ( btnNovoSelected.get() ) {
            
            //Cadastro de novos usuarios
            if ( salvarUsuario(modelUsuario) > 0 ) {
                showAlert(Alert.AlertType.INFORMATION, "Produto cadastrado com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar o produto");
            }
            setStatusTelaExibir(-2);
            setModelUsuarioBinds(tabelaUsuarios.getSelectionModel().getSelectedItem() );
        }
        else if ( btnEditarSelected.get() ) {
            
            //Atualiza o produto selecionado na tabela
            if ( atualizarUsuario(modelUsuario) ) {
                showAlert(Alert.AlertType.INFORMATION, "Atualizações salvas com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar as atualizações");
            }

            setStatusTelaExibir(-2);
            setModelUsuarioBinds(tabelaUsuarios.getSelectionModel().getSelectedItem() );
        }
    }
    
    @FXML
    private void handleBtnCancelarAction (ActionEvent event) {
        //Retorna para p mode de exibição, e no produto selecionado na tabela
        setStatusTelaExibir( tabelaUsuarios.getSelectionModel().getSelectedIndex() );
        setModelUsuarioBinds(tabelaUsuarios.getSelectionModel().getSelectedItem() );
    }
    
    @FXML
    private void handleBtnRemoveFiltro (ActionEvent event) {
        edtPesquisar.clear();
        edtPesquisar.requestFocus();
    }
    
    /* ====================================================================== */
    
     public void showAlert(Alert.AlertType tipo, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        
        alert.show();
    }
    
    public ButtonBar.ButtonData confirmAlert(String texto) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        alert.getButtonTypes().setAll( 
                new ButtonType("Sim", ButtonBar.ButtonData.YES), 
                new ButtonType("Não", ButtonBar.ButtonData.NO) );
        
        return alert.showAndWait().get().getButtonData();
    }
}
