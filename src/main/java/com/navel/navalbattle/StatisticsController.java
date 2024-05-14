package com.navel.navalbattle;

import com.navel.navalbattle.database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StatisticsController {
    @FXML
    TableView<Statistics> statTable;
    @FXML
    private void initialize() {

        if (DatabaseConnector.getConnection() != null) {
            try {
                String query = "SELECT *\n" +
                        "FROM statistics\n" +
                        "WHERE player_id = ?;";

                PreparedStatement statement = DatabaseConnector.getConnection().prepareStatement(query);
                statement.setInt(1, DatabaseConnector.getUserId());
                statement.execute();

                ResultSet resultSet = statement.getResultSet();
                resultSet.next();

                TableColumn<Statistics, String> nameColumn = new TableColumn<>("Name");
                TableColumn<Statistics, String> valueColumn = new TableColumn<>("Value");

                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));

                nameColumn.prefWidthProperty().bind(statTable.widthProperty().divide(2));
                valueColumn.prefWidthProperty().bind(statTable.widthProperty().divide(2));

                statTable.getColumns().addAll(nameColumn, valueColumn);

                statTable.getItems().add(new Statistics("Wins", String.valueOf(resultSet.getInt("victories"))));
                statTable.getItems().add(new Statistics("Losses", String.valueOf(resultSet.getInt("losses"))));
                statTable.getItems().add(new Statistics("Hits", String.valueOf(resultSet.getInt("hits"))));
                statTable.getItems().add(new Statistics("Total shots", String.valueOf(resultSet.getInt("total_shots"))));;

                String accuracy = String.format("%.2f", ((double) resultSet.getInt("hits") * 100 / resultSet.getInt("total_shots")));
                statTable.getItems().add(new Statistics("Accuracy", accuracy + '%'));

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public class Statistics {
        private String name;
        private String value;

        public Statistics(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public String getValue() {
            return value;
        }
    }
}
