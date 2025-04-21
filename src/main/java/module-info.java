module com.example.pecl_zombis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.pecl_zombis to javafx.fxml;
    exports com.example.pecl_zombis;
}