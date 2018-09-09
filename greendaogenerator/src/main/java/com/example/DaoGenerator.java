package com.example;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;


public class DaoGenerator {

    public static void main(String[] args) {

        Schema schema = new Schema(5, "cashflowdb");
        createServiceAppSchema(schema);
        try {
            new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static void createServiceAppSchema(Schema schema) {

        Entity userlogininfo = schema.addEntity("UserInfo");
        userlogininfo.addStringProperty("userid").primaryKey();
        userlogininfo.addStringProperty("username");
        userlogininfo.addStringProperty("password");
        userlogininfo.addStringProperty("firstname");
        userlogininfo.addStringProperty("lastname");
        userlogininfo.addStringProperty("picname");

        // Financial Year
        Entity financialyear = schema.addEntity("FinancialYear");
        financialyear.addIdProperty().autoincrement().primaryKey();
        financialyear.addDateProperty("startdate");
        financialyear.addDateProperty("enddate");
        financialyear.addStringProperty("competed");


        // Initial budget
        Entity initalbalance = schema.addEntity("InitialBudget");
        initalbalance.addStringProperty("bugetID").notNull().primaryKey();
        initalbalance.addStringProperty("moneytype");
        initalbalance.addDoubleProperty("pound");
        initalbalance.addDateProperty("startdate");
        initalbalance.addStringProperty("comment");

        // Categories
        Entity category = schema.addEntity("Category");
        category.addIdProperty().autoincrement().primaryKey();
        category.addStringProperty("catname");
        category.addStringProperty("inorexp");


        // Source
        Entity source = schema.addEntity("Source");
        Property sourceID = source.addIdProperty().autoincrement().primaryKey().getProperty();//source.addStringProperty("sourceid").primaryKey().autoincrement().getProperty();
        source.addStringProperty("sourcename");
        source.addBooleanProperty("isChecked");
        source.addBooleanProperty("isMandatory");
        Property categoryID = source.addLongProperty("categoryid").notNull().getProperty();
        source.addToOne(category, categoryID);
        category.addToMany(source, categoryID);


        // How Often or frequency to occur transection
        Entity howoften = schema.addEntity("HowOften");
        howoften.addIdProperty().autoincrement().primaryKey();
        howoften.addStringProperty("often");
        Property sourceIDTag = howoften.addLongProperty("sourceid").notNull().getProperty();
        howoften.addToOne(source, sourceIDTag);
        source.addToMany(howoften, sourceIDTag);


        // Transaction
        Entity transaction = schema.addEntity("InOutTransaction");
        transaction.addIdProperty().autoincrement().primaryKey();
        //transaction.addStringProperty("transactiondate");
        transaction.addDateProperty("transactiondate");
        transaction.addStringProperty("comment");
        transaction.addDoubleProperty("pound");
        transaction.addStringProperty("howoften");
        transaction.addDateProperty("nextdate");
        transaction.addBooleanProperty("isrepetitive");

        Property categoryid = transaction.addLongProperty("categoryid").notNull().getProperty();
        Property sourceid = transaction.addLongProperty("sourceid").notNull().getProperty();

        transaction.addToOne(source, sourceid);
        source.addToMany(transaction, sourceid);

        transaction.addToOne(category, categoryid);
        category.addToMany(transaction, categoryid);


        // Repeate Transaction
        Entity repeatTransaction = schema.addEntity("RepeatTransaction");
        repeatTransaction.addIdProperty().autoincrement().primaryKey();
        repeatTransaction.addDoubleProperty("pound");
        repeatTransaction.addDateProperty("transactiondate");
        repeatTransaction.addStringProperty("recomments");
        repeatTransaction.addBooleanProperty("confirm");
        repeatTransaction.addLongProperty("catID");

        Property actualtrasactionID = repeatTransaction.addLongProperty("actualtrasactionID").notNull().getProperty();
        repeatTransaction.addToOne(transaction, actualtrasactionID);
        transaction.addToMany(repeatTransaction, actualtrasactionID);


        // Repeate Transaction
        Entity dayInOutSummery = schema.addEntity("DayInOutSummery");
        dayInOutSummery.addIdProperty().autoincrement().primaryKey();
        dayInOutSummery.addDoubleProperty("income_pound");
        dayInOutSummery.addDoubleProperty("expense_pound");
        dayInOutSummery.addDoubleProperty("balance_in_hand");
        dayInOutSummery.addDateProperty("date");
        dayInOutSummery.addBooleanProperty("is_worst_case");
        dayInOutSummery.addBooleanProperty("is_debt");

        Entity subscription = schema.addEntity("Subscription");
        subscription.addIdProperty().autoincrement().primaryKey();
        subscription.addDateProperty("subscriptionStartDate");
        subscription.addDateProperty("subscriptionEndDate");
        subscription.addStringProperty("adminName");
        subscription.addBooleanProperty("isSubscribed");
        subscription.addStringProperty("adminPassword");
    }

}
