/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.controller;

import sicev.view.SICEV;
import sicev.DAO.DaoProdutos;
import sicev.model.ModelProdutos;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
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
public class ProdutosController implements Initializable {
    
    @FXML private GridPane paneCampos;
    @FXML private VBox tableContainer;
    @FXML private TableView<ModelProdutos> tabelaProdutos;
    @FXML private ToggleButton btnNovo;
    @FXML private ToggleButton btnEditar;
    @FXML private Button btnExcluir;
    @FXML private Button btnCancelar;
    @FXML private Button btnSalvar;
    @FXML private TextField edtCodigo;
    @FXML private TextField edtValor;
    @FXML private TextField edtEstoque;
    @FXML private TextField edtEstoqueMin;
    @FXML private TextField edtNome;
    @FXML private TextField edtPesquisar;
    
    private final DaoProdutos daoProduto = new DaoProdutos();
    private final ModelProdutos modelProdutos = new ModelProdutos();
    
    private final BooleanProperty tableSelectionIsNull = new SimpleBooleanProperty();
    private final BooleanProperty btnNovoSelected = new SimpleBooleanProperty();
    private final BooleanProperty btnEditarSelected = new SimpleBooleanProperty();
    
    private ObservableList<ModelProdutos> listaProdutos;
    private FilteredList<ModelProdutos> filteredListProds;
    
    private SICEV application;
    
    /**
     * Expressão lambda que implementa o bind dos campos com a linha selecionada
     * da tabela
     */
    final ChangeListener<ModelProdutos> TableListener = 
        (ObservableValue<? extends ModelProdutos> observable, 
                ModelProdutos oldValue, ModelProdutos newValue) -> {
            
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
        
        tableSelectionIsNull.bind( tabelaProdutos.getSelectionModel().selectedItemProperty().isNull() );
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
                        setModelProdutosBinds(new ModelProdutos());
                    }
                });
        
        
        //Cria um bind bidirecional entre os campos e o objeto "modelProdutos"
        edtNome.textProperty().bindBidirectional(
                modelProdutos.proNomeProperty());

        edtCodigo.textProperty().bindBidirectional(
                modelProdutos.idProdutoProperty(), new NumberStringConverter());

        edtValor.textProperty().bindBidirectional(
                modelProdutos.proValorProperty(), new NumberStringConverter());

        edtEstoque.textProperty().bindBidirectional(
                modelProdutos.proEstoqueProperty(), new NumberStringConverter());

        edtEstoqueMin.textProperty().bindBidirectional(
                modelProdutos.proEstoqueMinProperty(), new NumberStringConverter());
        
        
        //Bind automático dos campos ao selecionar uma linha na tabela
        tabelaProdutos.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
        
        //Set the filter Predicate whenever the filter changes.
        edtPesquisar.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredListProds.setPredicate(produto -> {
                // If filter text is empty, display all produtos.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (produto.getProNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (produto.getProNome().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        
        //Configura a tela
        setStatusTelaExibir();        
    }
    
    public void setApp (SICEV application) {
        this.application = application;
    }
    
    public void carregarProdutos() {
        listaProdutos = FXCollections.observableList(getAllProdutos());
        
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        filteredListProds = new FilteredList<>(listaProdutos, p -> true);

        // 2. Wrap the FilteredList in a SortedList. 
        SortedList<ModelProdutos> sortedData = new SortedList<>(filteredListProds);

        // 3. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(tabelaProdutos.comparatorProperty());    
        
        //Remove o changeListener da tabela enquanto atualiza seus items
        tabelaProdutos.getSelectionModel().selectedItemProperty().
                removeListener(TableListener);
        
        // 4. Add sorted (and filtered) data to the table.
        tabelaProdutos.setItems(sortedData);
        
        tabelaProdutos.getSelectionModel().selectedItemProperty().
                addListener(TableListener);
    }
    
    private void setModelProdutosBinds (ModelProdutos currentProd) {
        if ( currentProd != null ) {
            modelProdutos.idProdutoProperty().bind(currentProd.idProdutoProperty());
            modelProdutos.proNomeProperty().bind(currentProd.proNomeProperty());
            modelProdutos.proValorProperty().bind(currentProd.proValorProperty());
            modelProdutos.proEstoqueProperty().bind(currentProd.proEstoqueProperty());
            modelProdutos.proEstoqueMinProperty().bind(currentProd.proEstoqueMinProperty());
        } 
        else {
            modelProdutos.idProdutoProperty().unbind();
            modelProdutos.proNomeProperty().unbind();
            modelProdutos.proValorProperty().unbind();
            modelProdutos.proEstoqueProperty().unbind();
            modelProdutos.proEstoqueMinProperty().unbind();
        }
    } 
    
    private void setStatusTelaExibir () {
        setStatusTelaExibir(-1);
    }
    
    /**
     * <p>para selecionar a última linha passe -2 como parâmetro</p>
     * <p>para selecionar a primeiro linha passe -1 como parâmetro</p>
     * @param tableRowIndex 
     */
    private void setStatusTelaExibir (int tableRowIndex) {
        btnNovo.setSelected(false);
        btnEditar.setSelected(false);
        
        switch (tableRowIndex) {
            case -2:
                tabelaProdutos.getSelectionModel().selectLast();
                break;
            case -1:
                tabelaProdutos.getSelectionModel().selectFirst();
                break;
            default:
                tabelaProdutos.getSelectionModel().select(tableRowIndex);
                break;
        }
        
        tabelaProdutos.requestFocus();
        
        /**
         * faz um bind com a linha atual da tabela, caso o evento ChangeLister
         * da tabela não seja invocado
         */        
        setModelProdutosBinds( tabelaProdutos.getSelectionModel().getSelectedItem() );

        //Devolve os eventos padrões dos botões Editar e Novo
        btnNovo.setOnAction((e) -> {handleBtnNovoAction(e);});
        btnEditar.setOnAction((e) -> {handleBtEditarAction(e);});
    }
    
    /* ====================== Métodos da classe Dao ========================= */
    public int salvarProdutos(ModelProdutos pModelProduto) {
        return daoProduto.salvarProdutosDAO(pModelProduto);
    }
    
    /**
     * exclue um produto a partir do Id passado
     * @param pIdProduto
     * @return 
     */
    public boolean excluirProduto(int pIdProduto) {
        return daoProduto.excluirProdutoDAO(pIdProduto);
    }
    
    /**
     * atualiza o produto passado em forma de objeto
     * @param pModelProduto
     * @return 
     */
    public boolean atualizarProduto(ModelProdutos pModelProduto) {
        return daoProduto.atualizarProdutoDAO(pModelProduto);
    }
    
    /**
     * 
     * @param pIdProduto
     * @return um objeto ModelProdutos com os dados do produto com o Id passado
     */
    public ModelProdutos getProdutoById( int pIdProduto ) {
        return daoProduto.getProdutoByIdDao(pIdProduto);
    }
    
    /**
     * Pega todos os produtos da tabela 
     * @return 
     */
    public ArrayList<ModelProdutos> getAllProdutos() {
        return daoProduto.getAllProdutosDao();
    }
    /* ====================================================================== */
    
    /* ==================== Eventos da Interface ============================ */
    @FXML
    private void handleBtnNovoAction (ActionEvent event) {        
        
        //passa um ModelProduto vazio para limpar os campos
        setModelProdutosBinds( new ModelProdutos() );
        
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
        
        if (confirmAlert("Confirmar a exclusão do produto?") == ButtonBar.ButtonData.YES) {
            
            int newIndex = 0;
            
            if ( excluirProduto(tabelaProdutos.getSelectionModel().
                    getSelectedItem().getIdProduto()) ) {
                
                //Guarda o index da linha anterior ou posterior à linha excluída
                int curIndex = tabelaProdutos.getSelectionModel().getSelectedIndex();
                if ( curIndex ==  0) {
                    tabelaProdutos.getSelectionModel().selectNext();
                }
                else {
                    tabelaProdutos.getSelectionModel().selectPrevious();
                }
                newIndex = tabelaProdutos.getSelectionModel().getSelectedIndex();
                
                //Recarrega os produtos do banco
                carregarProdutos();
                
                showAlert(AlertType.INFORMATION, "Produto excuído com sucesso!");
            }
            else {
                showAlert(AlertType.WARNING, "Não foi possível excluir o produto!");
            }
            
            //Deixa a tela no mode de exibição
            setStatusTelaExibir(newIndex);
        }
    }
    
    @FXML
    private void handleBtnSalvarAction (ActionEvent event) {
        
        if ( btnNovoSelected.get() ) {
            
            //Cadastro de novos produtos
            if ( salvarProdutos(modelProdutos) > 0 ) {
                showAlert(AlertType.INFORMATION, "Produto cadastrado com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(AlertType.WARNING, "Não foi possível cadastrar o produto");
            }
            setStatusTelaExibir(-2);
            setModelProdutosBinds( tabelaProdutos.getSelectionModel().getSelectedItem() );
        }
        else if ( btnEditarSelected.get() ) {
            
            //Atualiza o produto selecionado na tabela
            if ( atualizarProduto(modelProdutos) ) {
                showAlert(AlertType.INFORMATION, "Alterações salvas com sucesso!");
                carregarProdutos();
            }   
            else {
                showAlert(AlertType.WARNING, "Não foi possível cadastrar as alterações");
            }

            setStatusTelaExibir(-2);
            setModelProdutosBinds( tabelaProdutos.getSelectionModel().getSelectedItem() );
        }
    }
    
    @FXML
    private void handleBtnCancelarAction (ActionEvent event) {
        //Retorna para p mode de exibição, e no produto selecionado na tabela
        setStatusTelaExibir( tabelaProdutos.getSelectionModel().getSelectedIndex() );
        setModelProdutosBinds( tabelaProdutos.getSelectionModel().getSelectedItem() );
    }
    
    @FXML
    private void handleBtnRemoveFiltro (ActionEvent event) {
        edtPesquisar.clear();
        edtPesquisar.requestFocus();
    }
    /* ====================================================================== */
    
    private void showAlert(AlertType tipo, String texto) {
        Alert alert = new Alert(tipo);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        
        alert.show();
    }
    
    private ButtonBar.ButtonData confirmAlert(String texto) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("SICEV");
        alert.setHeaderText("Produtos");
        alert.setContentText(texto);
        alert.getButtonTypes().setAll( 
                new ButtonType("Sim", ButtonBar.ButtonData.YES), 
                new ButtonType("Não", ButtonBar.ButtonData.NO) );
        
        return alert.showAndWait().get().getButtonData();
    }
    
}
