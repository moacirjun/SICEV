/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import sicev.view.SICEV;
import sicev.DAO.DaoClientes;
import sicev.model.ModelClientes;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.converter.NumberStringConverter;

/**
 * FXML Controller class
 *
 * @author Moacir
 */
public class ClientesController implements Initializable {

    @FXML private GridPane paneCampos;
    @FXML private VBox tableContainer;
    @FXML private TableView<ModelClientes> tabelaClientes;
    @FXML private ToggleButton btnNovo;
    @FXML private ToggleButton btnEditar;
    @FXML private TextField edtPesquisar;
    @FXML private Button btnRemoveFiltro;
    @FXML private Button btnExcluir;
    @FXML private Button btnSalvar;
    @FXML private Button btnCancelar;
    
    @FXML private TextField edtCodigo;
    @FXML private TextField edtNome;
    @FXML private TextField edtEndereco;
    @FXML private TextField edtBairro;
    @FXML private TextField edtCidade;
    @FXML private ComboBox cmbUf;
    @FXML private TextField edtCep;
    @FXML private TextField edtTelefone;
    
    private final DaoClientes daoClientes = new DaoClientes();
    private final ModelClientes modelClientes = new ModelClientes();
    
    private final BooleanProperty tableSelectionIsNull = new SimpleBooleanProperty();
    private final BooleanProperty btnNovoSelected = new SimpleBooleanProperty();
    private final BooleanProperty btnEditarSelected = new SimpleBooleanProperty();
    
    private ObservableList<ModelClientes> listaClientes;
    private FilteredList<ModelClientes> filteredListClientes;
    
    private SICEV application;
    
    /**
     * Expressão lambda que implementa o bind dos campos com a linha selecionada
     * da tabela
     */
    final ChangeListener<ModelClientes> TableListener = 
        (ObservableValue<? extends ModelClientes> observable, 
                ModelClientes oldValue, ModelClientes newValue) -> {
            
            setModelProdutosBinds(newValue);
        };
    
    /**
     * Initializes the controller class.
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Carrega a tabela
        carregarProdutos();
        
        tableSelectionIsNull.bind( tabelaClientes.getSelectionModel().selectedItemProperty().isNull() );
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
                        setModelProdutosBinds(new ModelClientes());
                    }
                });
        
        
        //Cria um bind bidirecional entre os campos e o objeto "modelclientes"
        edtCodigo.textProperty().bindBidirectional(
                modelClientes.idClienteProperty(), new NumberStringConverter());
        
        edtNome.textProperty().bindBidirectional(modelClientes.nomeProperty());
        edtEndereco.textProperty().bindBidirectional(modelClientes.enderecoProperty());
        edtBairro.textProperty().bindBidirectional(modelClientes.bairroProperty());
        edtCidade.textProperty().bindBidirectional(modelClientes.cidadeProperty());
        //cmbUf.selectionModelProperty().bindBidirectional(modelClientes.ufProperty());
        edtCep.textProperty().bindBidirectional(modelClientes.cepProperty());
        edtTelefone.textProperty().bindBidirectional(modelClientes.telefoneProperty());
        
        
        //Bind automático dos campos ao selecionar uma linha na tabela
        tabelaClientes.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
        
        //Set the filter Predicate whenever the filter changes.
        edtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredListClientes.setPredicate(cliente -> {
                // If filter text is empty, display all produtos.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare name and cidade of every cliente with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (cliente.getNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (cliente.getCidade().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        //Configura a tela
        setStatusTelaExibir(); 
    }  
    
    public void setApp (SICEV app) {
        this.application = app;
    }
    
    public void carregarProdutos() {
        listaClientes = FXCollections.observableList(getAllClientes());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredListClientes = new FilteredList<>(listaClientes, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelClientes> sortedData = new SortedList<>(filteredListClientes);

        // 3. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tabelaClientes.comparatorProperty());    
        
        //Remove o changeListener da tabela enquanto atualiza seus items
        tabelaClientes.getSelectionModel().selectedItemProperty().
                removeListener(TableListener);
        
        // 4. Add sorted (and filtered) data to the table.
        tabelaClientes.setItems(sortedData);
        
        tabelaClientes.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }
    
    private void setModelProdutosBinds (ModelClientes currentProd) {
        if ( currentProd != null ) {
            modelClientes.idClienteProperty().bind(currentProd.idClienteProperty());
            modelClientes.nomeProperty().bind(currentProd.nomeProperty());
            modelClientes.enderecoProperty().bind(currentProd.enderecoProperty());
            modelClientes.bairroProperty().bind(currentProd.bairroProperty());
            modelClientes.cidadeProperty().bind(currentProd.cidadeProperty());
            modelClientes.ufProperty().bind(currentProd.ufProperty());
            modelClientes.cepProperty().bind(currentProd.cepProperty());
            modelClientes.telefoneProperty().bind(currentProd.telefoneProperty());
        } 
        else {
            modelClientes.idClienteProperty().unbind();
            modelClientes.nomeProperty().unbind();
            modelClientes.enderecoProperty().unbind();
            modelClientes.bairroProperty().unbind();
            modelClientes.cidadeProperty().unbind();
            modelClientes.ufProperty().unbind();
            modelClientes.cepProperty().unbind();
            modelClientes.telefoneProperty().unbind();
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
                tabelaClientes.getSelectionModel().selectLast();
                break;
            case -1:
                tabelaClientes.getSelectionModel().selectFirst();
                break;
            default:
                tabelaClientes.getSelectionModel().select(tableRowIndex);
                break;
        }
        
        tabelaClientes.requestFocus();
        
        /**
         * faz um bind com a linha atual da tabela, caso o evento ChangeLister
         * da tabela não seja invocado
         */        
        setModelProdutosBinds( tabelaClientes.getSelectionModel().getSelectedItem() );

        //Devolve os eventos padrões dos botões Editar e Novo
        btnNovo.setOnAction((e) -> {handleBtnNovoAction(e);});
        btnEditar.setOnAction((e) -> {handleBtEditarAction(e);});
    }
    
    
    /* ====================== Métodos da classe Dao ========================= */
    private int salvarCliente (ModelClientes pModelClientes) {
        return  this.daoClientes.salvarClienteDAO(pModelClientes);
    }
    
    private boolean excluirCliente (int idCliente) {
        return this.daoClientes.excluirClienteDao(idCliente);
    }
    
    private boolean atualizarCliente (ModelClientes pModelCliente) {
        return this.daoClientes.atualizarClienteDao(pModelCliente);
    }
    
    private ModelClientes getClienteById (int idCliente) {
        return this.daoClientes.getClienteByIdDao(idCliente);
    }
    
    private ArrayList<ModelClientes> getAllClientes() {
        return this.daoClientes.getAllClientesDao();
    }
    /* ====================================================================== */
    
    /* ==================== Eventos da Interface ============================ */
    @FXML
    private void handleBtnNovoAction (ActionEvent event) {        
        
        //passa um ModelCliente vazio para limpar os campos
        setModelProdutosBinds( new ModelClientes() );
        
        //Retira o bind do modelProdutos com esse ModelProduto vazio
        setModelProdutosBinds(null);
        
        //Comfiguração dos componentes da tela
        edtNome.requestFocus();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnNovo.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtEditarAction (ActionEvent event) {
        //Retira o bind do modelProdutos para que seja possível a alteração
        setModelProdutosBinds(null);
        
        edtNome.requestFocus();
        edtNome.selectEnd();
        
        //No próximo click o botao executa a ação "Cancelar"
        btnEditar.setOnAction((e) -> { handleBtnCancelarAction(e); });
    }
    
    @FXML
    private void handleBtnExcluirAction (ActionEvent event) {
        
        if (confirmAlert("Confirmar a exclusão do cliente?") == ButtonBar.ButtonData.YES) {
            
            int newIndex = 0;
            
            if ( excluirCliente(tabelaClientes.getSelectionModel().
                    getSelectedItem().getIdCliente()) ) {
                
                //Guarda o index da linha anterior ou posterior à linha excluída
                int curIndex = tabelaClientes.getSelectionModel().getSelectedIndex();
                if ( curIndex ==  0) {
                    tabelaClientes.getSelectionModel().selectNext();
                }
                else {
                    tabelaClientes.getSelectionModel().selectPrevious();
                }
                newIndex = tabelaClientes.getSelectionModel().getSelectedIndex();
                
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
            
            //Cadastro de novos produtos
            if ( salvarCliente(modelClientes) > 0 ) {
                showAlert(Alert.AlertType.INFORMATION, "Produto cadastrado com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar o produto");
            }
            setStatusTelaExibir(-2);
            setModelProdutosBinds( tabelaClientes.getSelectionModel().getSelectedItem() );
        }
        else if ( btnEditarSelected.get() ) {
            
            //Atualiza o produto selecionado na tabela
            if ( atualizarCliente(modelClientes) ) {
                showAlert(Alert.AlertType.INFORMATION, "Atualizações salvas com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(Alert.AlertType.WARNING, "Não foi possível cadastrar as atualizações");
            }

            setStatusTelaExibir(-2);
            setModelProdutosBinds( tabelaClientes.getSelectionModel().getSelectedItem() );
        }
    }
    
    @FXML
    private void handleBtnCancelarAction (ActionEvent event) {
        //Retorna para p mode de exibição, e no produto selecionado na tabela
        setStatusTelaExibir( tabelaClientes.getSelectionModel().getSelectedIndex() );
        setModelProdutosBinds( tabelaClientes.getSelectionModel().getSelectedItem() );
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
