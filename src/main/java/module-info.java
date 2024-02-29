module com.jbunce.analizadorlexico {

    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;
    requires org.fxmisc.undo;
    requires lombok;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires org.slf4j;
    requires jflex;

    opens com.jbunce.analizadorlexico to javafx.fxml;

    exports com.jbunce.analizadorlexico;
    exports com.jbunce.analizadorlexico.logger;
    opens com.jbunce.analizadorlexico.logger to javafx.fxml;
    exports com.jbunce.analizadorlexico.controllers;
    opens com.jbunce.analizadorlexico.controllers to javafx.fxml;
}