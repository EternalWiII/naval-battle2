module com.navel.navalbattle {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.postgresql.jdbc;
    requires com.zaxxer.hikari;
    requires java.desktop;


    opens com.navel.navalbattle to javafx.fxml;
    exports com.navel.navalbattle;
    exports com.navel.navalbattle.ships;
    opens com.navel.navalbattle.ships to javafx.fxml;
    exports com.navel.navalbattle.records;
    opens com.navel.navalbattle.records to javafx.fxml;
    exports com.navel.navalbattle.interfaces;
    opens com.navel.navalbattle.interfaces to javafx.fxml;
//    opens com.navel.navalbattle.controllers to javafx.fxml;
}