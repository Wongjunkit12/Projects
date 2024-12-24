module com.fierydragon {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    opens com.fierydragon to javafx.fxml;
    exports com.fierydragon;
    exports com.fierydragon.volcano;
    opens com.fierydragon.volcano to javafx.fxml;
    exports com.fierydragon.display;
    opens com.fierydragon.display to javafx.fxml;
    exports com.fierydragon.display.animals;
    opens com.fierydragon.display.animals to javafx.fxml;
    exports com.fierydragon.display.creations;
    opens com.fierydragon.display.creations to javafx.fxml;
    opens com.fierydragon.components to javafx.fxml;
    exports com.fierydragon.components;
    exports com.fierydragon.pieces;
    opens com.fierydragon.pieces to javafx.fxml;
}
