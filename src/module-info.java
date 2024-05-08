/**
 * 
 */
/**
 * @author khattam
 *
 */
//module Research {
//	 requires gs.core;
//	    requires gs.ui.swing;
//	    
//	    // Other required modules
//	    requires java.base;
//	    requires java.desktop;
//	    
//}

//module Research {
//    requires gs.core;
//    requires gs.ui.swing;
//
//    // JavaFX modules
//    requires javafx.controls;
//    requires javafx.fxml; // Include this if you are using FXML
//    requires javafx.graphics; // This is typically required as well
//
//    // Other required modules
//    requires java.base;
//    requires java.desktop;
//
//    // Add exports for your packages that contain controllers or main class
//    exports banking;
//}

module Research {
	requires gs.core;
    requires gs.ui.swing;
    requires javafx.controls;
    requires javafx.fxml;
    
    // Export your packages if needed
    exports banking;
}
