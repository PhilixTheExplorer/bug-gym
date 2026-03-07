module dev.philixtheexplorer.buggym {
    requires transitive javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.web;
    requires javafx.graphics;

    requires org.commonmark;
    requires org.commonmark.ext.gfm.tables;

    requires transitive org.fxmisc.richtext;
    requires transitive reactfx;
    requires transitive org.fxmisc.flowless;

    requires java.compiler;
    requires transitive java.net.http;

    opens dev.philixtheexplorer.buggym to javafx.fxml;
    opens dev.philixtheexplorer.buggym.model to javafx.base;
    opens dev.philixtheexplorer.buggym.ui to javafx.fxml;

    exports dev.philixtheexplorer.buggym;
    exports dev.philixtheexplorer.buggym.model;
    exports dev.philixtheexplorer.buggym.service;
    exports dev.philixtheexplorer.buggym.ui;
}
