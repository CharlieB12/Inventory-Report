package osu.cse2123;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A program that generates an inventory report
 * 
 * @name Charlie Britt
 * @version 10/17/2023
 * 
 */

public class InventoryReport {
	
	
	/**
	 * converts the product string from text file to an ArrayList
	 * 
	 * @param line from user txt file with product as a string
	 * @return the product as a list
	 */
	
	public static ArrayList<String> convert_list(String line){
		//split the string based "," and assign to array
		String[] prod_arr = line.split(",");
		ArrayList<String> prod_list = new ArrayList<>();
		
		//for every item in array, add to array list
		for (String item : prod_arr) {
			prod_list.add(item);
		}
		return prod_list;
	}
	
	
	/**
	 * Creates a product object based on a specific line from
	 * user input file.
	 * 
	 * @param product as an ArrayList from user txt file
	 * @return Product object based on list
	 */
	
    public static Product create_product(ArrayList<String> product) {
    	Product new_prod = new Product();
    	//sets each parameter of product object based on list
    	new_prod.setName(product.get(0));
    	new_prod.setInventoryCode(product.get(1));
    	new_prod.setQuantity(Integer.parseInt(product.get(2)));
    	new_prod.setPrice(Integer.parseInt(product.get(3)));
    	new_prod.setType(product.get(4));
    	//adds ratings
    	for(int i = 5; i < product.size(); i++) {
    		new_prod.addUserRating(Integer.parseInt(product.get(i)));
    	}
    	return new_prod;
    }
	
	
	
    /**
	 * Reads the user input file and creates a list of product objects
	 * 
	 * @param fileName user input txt file with products
	 * @return ArrayList of product objects
	 */
	public static ArrayList<Product> product_list(String fileName) throws FileNotFoundException {
		ArrayList<Product> prod_list = new ArrayList<>();
		File textFile = new File(fileName);
		Scanner scan = new Scanner(textFile);
		
		//takes line in inventory, converts to arraylist, then converts the arraylist to a product
		//and adds it to product list.
		while(scan.hasNext()) {
			String line = scan.nextLine();
			ArrayList<String> product = convert_list(line);
			Product prod = create_product(product);
			prod_list.add(prod);
		}
		scan.close();
		return prod_list;
	}
	
	
	/**
	 * converts the average product rating into stars
	 * 
	 * @param rating the average rating of product
	 * @return the average rating of product in stars
	 */
	
	public static String convert_stars(int rating) {
		String star_rating = "";
		for(int j = 0; j < rating; j++) {
			star_rating += "*";
		}
		return star_rating;
	}
	
	
	
	/**
	 * Finds the highest rated product in the inventory list
	 * 
	 * @param prod_list list of product objects
	 * @return String with the highest rated product and its rating in stars
	 */
	
	public static String highest_rating(ArrayList<Product> prod_list) {
		int max = 0;
		String product_name = "";
		//loops through each product
		for(int i = 0; i < prod_list.size(); i++) {
			int prod_rating = prod_list.get(i).getAvgUserRating();
			//if the rating of product is greater than the previous product,
			//assign to msx and get its name
			if(prod_rating > max) {
				max = prod_rating;
				product_name = prod_list.get(i).getName();
			}
		}
		//convert rating to stars
		String stars = convert_stars(max);
		return product_name + " (" + stars + ")"; 
	}
	
	
	/**
	 * Finds the lowest rated product in the inventory list
	 * 
	 * @param prod_list list of product objects
	 * @return String with the lowest rated product and its rating in stars
	 */
	
	public static String lowest_rating(ArrayList<Product> prod_list) {
		int min = prod_list.get(0).getAvgUserRating();
		String product_name = "";
		//loops through each product
		for(int i = 1; i < prod_list.size(); i++) {
			int prod_rating = prod_list.get(i).getAvgUserRating();
			//if the rating of product is less than the previous product,
			//assign to min and get its name
			if(prod_rating < min) {
				min = prod_rating;
				product_name = prod_list.get(i).getName();
			}
		}
		//convert rating to stars
		String stars = convert_stars(min);
		return product_name + " (" + stars + ")";
	}
	
	
	/**
	 * Prints the final inventory report
	 * 
	 * @param prod_list the list of product objects
	 */
	
	public static void inventory_report(ArrayList<Product> prod_list) {
		//header section
		System.out.println("Product Inventory Summary Report");
		System.out.println("----------------------------------------------------------------------");
		System.out.print(String.format("%-25s %-10s %-4s %-6s %-6s %-6.6s %-6.6s%n", 
				"Product Name", "I Code", "Type", "Quant.", "Price", "Rating", "# Rat."));
		System.out.println("------------------------- ---------- ---- ------ ------ ------ ------");
		//detail section
		for(int i = 0; i < prod_list.size(); i++ ) {
			//grabs each element of product
			String name = prod_list.get(i).getName();
			String code = prod_list.get(i).getInventoryCode();
			String type = prod_list.get(i).getType();
			int quantity = prod_list.get(i).getQuantity();
			double price = prod_list.get(i).getPrice()/100;
			int num_rating = prod_list.get(i).getUserRatingCount();
			int ave_rating = prod_list.get(i).getAvgUserRating();
			//converts average rating into stars
			String star_rating = convert_stars(ave_rating);
			//formats the product line
			System.out.print(String.format("%-25.25s %-10.10s %-4.4s %6d %6.2f %6.6s %6d%n",
					name, code, type, quantity, price, star_rating, num_rating));
		}
		System.out.println("----------------------------------------------------------------------");
		System.out.println("Total products in database: " + prod_list.size());
		System.out.println("Highest Average Ranked Item: " + highest_rating(prod_list) + " ");
		System.out.println("Lowest Average Ranked Item: " + lowest_rating(prod_list) + " ");
		System.out.println("----------------------------------------------------------------------");
		
	}
	
	
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner scan = new Scanner(System.in);
		System.out.print("Enter an inventory filename: ");
		
		String inventory = scan.nextLine();
		//generates product objects based off of inventory
		ArrayList<Product> prod_list = product_list(inventory);
		
		//prints inventory report
		inventory_report(prod_list);
		
		scan.close();


	}

}
