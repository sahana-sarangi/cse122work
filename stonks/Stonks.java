// Sahana Sarangi
// 07/15/2024
// CSE 122
// P0: Stonks
// TA: Connor & Abby
// This class allows the user to buy, sell, and save stocks. It takes in a file
// containing the stocks on the market and their prices and uses that information
// to allow users to buy and sell stock available, as well as save their portfolio.

import java.util.*;
import java.io.*;

public class Stonks {
    public static final String STOCKS_FILE_NAME = "stonks.tsv";

    public static void main(String[] args) throws FileNotFoundException {
        Scanner console = new Scanner(System.in);
        Scanner fileScan = new Scanner(new File(STOCKS_FILE_NAME));

        int numStocks = Integer.parseInt(fileScan.nextLine());
        String[] stocks = new String[numStocks];
        double[] prices = new double[numStocks];
        double[] portfolio = new double[numStocks];

        fileScan.nextLine();

        System.out.println("Welcome to the CSE 122 Stocks Simulator!");
        System.out.println("There are " + numStocks + " stocks on the market:");

        loadData(fileScan, stocks, prices, numStocks);
    
        String menuChoice = "";
        boolean loopRunning = true;
        while (loopRunning) {
            System.out.println("Menu: (B)uy, (Se)ll, (S)ave, (Q)uit");
            System.out.println("Enter your choice: ");
            menuChoice = console.nextLine();

            if (menuChoice.equalsIgnoreCase("q")) {
                loopRunning = false;

            } else if (menuChoice.equalsIgnoreCase("b")) {
                System.out.print("Enter the stock ticker: ");
                String tickerToBuy = console.nextLine();
                System.out.print("Enter your budget: ");
                String budgetInput = console.nextLine();
                double budget = Double.parseDouble(budgetInput);

                buyStock(budget, stocks, prices, portfolio, tickerToBuy);

            } else if (menuChoice.equalsIgnoreCase("se")) {
                
                System.out.print("Enter the stock ticker: ");
                String tickerToSell = console.nextLine();
                System.out.print("Enter the number of shares to sell: ");
                String sharesToSellInput = console.nextLine();
                double sharesToSell = Double.parseDouble(sharesToSellInput);
                
                sellStock(tickerToSell, portfolio, sharesToSell, stocks);

            } else if (menuChoice.equalsIgnoreCase("s")) {
                System.out.print("Enter new portfolio file name: ");
                String portfolioName = console.nextLine();
                
                save(portfolioName, portfolio, stocks);

            } else {
                System.out.println("Invalid choice: " + menuChoice);
                System.out.println("Please try again");
            }    
        }

        System.out.println();
        System.out.println("Your portfolio is currently valued at: $" + 
                calculatePortfolioValue(portfolio, prices));
        
    }

    //Behavior:
    //  - This method populates two empty arrays--stocks and prices--to contain
    //    the tickers of the stocks on the market and their prices using data from
    //    the stonks.tsv file. Simultaneously, it displays each of the stocks on 
    //    the market and the price of one share of each stock to the user.
    //Paramaters:
    //  - fileScan: a scanner that scans over stonks.tsv
    //  - stocks: empty array that will contain the tickers of the stocks on the market
    //  - prices: empty array that will contain the price of a share of each of the 
    //            stocks on the market
    //  - numStocks: the number of stocks on the market
    public static void loadData(Scanner fileScan, String[] stocks, double[] prices, 
            int numStocks) {
        for (int i = 0; i < numStocks; i++) {
            String line = fileScan.nextLine();
            Scanner lineScan = new Scanner(line);
            String ticker = lineScan.next();
            double price = lineScan.nextDouble();
            stocks[i] = ticker;
            prices[i] = price;
            System.out.println(ticker + ": " + price);
        }
    }

    //Behavior:
    //  - This method buys as many shares as possible of a certain stock on the 
    //    market (chosen by the user), depending on the user's budget. If the user's
    //    budget is at least $5, the method updates the user's portfolio by adding the 
    //    number of shares of the stock the user's budget is able to buy and tells
    //    the user that they have bought their requested stock successfully. Otherwise,
    //    it tells the user that their budget must be at least $5.
    //Parameters:
    //  - budget: the user's budget for buying their choice of stock
    //  - stocks: an array containing the tickers of all the stocks on the market
    //  - prices: an array containing the prices of all the stocks on the market
    //  - portfolio: an array containing the amount of shares that the user has
    //               of each stock
    //  - tickerToBuy: the ticker of the stock that the user wants to buy
    public static void buyStock(double budget, String[] stocks, double[] prices, 
            double[] portfolio, String tickerToBuy) {
        if (budget < 5) {
            System.out.println("Budget must be at least $5");
        } else {
            for (int a = 0; a < stocks.length; a++) {
                if (tickerToBuy.equalsIgnoreCase(stocks[a])) {
                    portfolio[a] += (budget/prices[a]);
                    System.out.print("You successfully bought " + stocks[a] + ".");
                    System.out.println();
                }
            }
        }
    }

    //Behavior:
    //  - This method sells the number of shares of a stock that the user wants 
    //    to sell. If the user has enough shares of their requested stock in their
    //    portfolio, the number of shares they want to sell is subtracted from their 
    //    portfolio, and they are told that they have successfully sold the stock
    //    they wanted to sell. Otherwise, they are told they do not have enough shares
    //    of that stock to sell the number of shares they want to.
    //Parameters:
    //  - tickerToSell: the ticker of the stock the user wants to sell
    //  - portfolio: an array containing the amount of shares that the user has 
    //               of each stock
    //  - sharesToSell: the number of shares the user wants to sell of a certain stock
    //  - stocks: an array containing the tickers of all the stocks on the market
    public static void sellStock(String tickerToSell, double[] portfolio, double sharesToSell,
            String[] stocks) {
        for (int b = 0; b < stocks.length; b++) {
            if (tickerToSell.equalsIgnoreCase(stocks[b])) {
                if (portfolio[b] >= sharesToSell) {
                    portfolio[b] -= sharesToSell;
                    System.out.println("You successfully sold " + sharesToSell + 
                            " shares of " + tickerToSell + ".");
                } else { 
                    System.out.println("You do not have enough shares of " + tickerToSell +
                            " to sell " + sharesToSell + " shares.");
                }
            }
        }
    }

    //Behavior:
    //  - This method saves the user's portfolio in a valid file of their choice. 
    //    The resulting file contains the ticker of each stock the user owns and
    //    the amount of shares of that stock that the user owns.
    //Parameters:
    //  - portfolioName: the name of the portfolio file that the user wants
    //  - portfolio: an array containing the amount of shares that the user has of 
    //               each stock
    //  - stocks: an array containing the tickers of all the stocks on the market
    public static void save(String portfolioName, double[] portfolio, String[] stocks) 
            throws FileNotFoundException {
        File savedPortfolio = new File(portfolioName);
        PrintStream output = new PrintStream(savedPortfolio);
        for (int j = 0; j < portfolio.length; j++) {
            if  (portfolio[j] != 0) {
                output.println(stocks[j] + " " + portfolio[j]);
            }
        }
    }

    //Behavior: 
    //  - This method calculates the total value of the user's portfolio by multiplying
    //    the number of shares the user has of each stock by the stock's price, then 
    //    adding together all of the products.
    //Paramters: 
    //  - portfolio: an array containing the amount of shares that the user has of each
    //               stock
    //  - prices: an array containing the prices of all the stocks on the market
    //Returns:
    //  - double: the total value of the portfolio
    public static double calculatePortfolioValue(double[] portfolio, double[] prices) {
        double portfolioValue = 0;
        for (int c = 0; c < portfolio.length; c++) {
            portfolioValue += (portfolio[c] * prices[c]);
        }
        return portfolioValue;
    }

    
}
