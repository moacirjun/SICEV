/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sicev.view;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sicev.controller.ClientesController;
import sicev.controller.MainController;
import sicev.controller.ProdutosController;

/**
 *
 * @author Moacir
 */
public class SICEV extends Application {
    
    private Stage mainStage;
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        mainStage.setTitle("SICEV");
        mainStage.setMaximized(true);
        
        gotoMainPage();
        
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void openProdutos() {
        try {
            Stage newStage = new Stage();
            
            ProdutosController produtos = 
                    (ProdutosController) loadSceneOnStage("Produtos.fxml", newStage);
            produtos.setApp(this);
            
            newStage.sizeToScene();
            newStage.setResizable(false);
            newStage.initOwner(mainStage);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
            
        } catch (Exception ex) {
            Logger.getLogger(SICEV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void openClientes() {
        try {
            Stage newStage = new Stage();
            
            ClientesController produtos = 
                    (ClientesController) loadSceneOnStage("Clientes.fxml", newStage);
            produtos.setApp(this);
            
            newStage.sizeToScene();
            newStage.setResizable(false);
            newStage.initOwner(mainStage);
            newStage.initModality(Modality.APPLICATION_MODAL);
            newStage.showAndWait();
            
        } catch (Exception ex) {
            Logger.getLogger(SICEV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void gotoMainPage() {
        try {
            MainController main = (MainController) loadSceneOnStage("Main.fxml");
            main.setApp(this);
        } catch (Exception ex) {
            Logger.getLogger(SICEV.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Initializable loadSceneOnStage(String fxml) throws Exception {
        return loadSceneOnStage(fxml, mainStage);
    }
    
    private Initializable loadSceneOnStage(String fxml, Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        InputStream in = SICEV.class.getResourceAsStream(fxml);
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(SICEV.class.getResource(fxml));
        Parent page;
        try {
            page = loader.load(in);
        } finally {
            in.close();
        } 
        Scene scene = new Scene(page);
        stage.setScene(scene);
        return (Initializable) loader.getController();
    }
    
}
