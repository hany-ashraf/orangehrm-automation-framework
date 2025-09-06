package util;

import java.io.*;
import java.util.*;

public class CSVComparerAutoDetect {
    public static synchronized void compareLatestTwoBySource(String directory, String sourceName) {
        try {
            File folder = new File(directory);
            File[] files = folder.listFiles((dir, name) ->
                    name.startsWith("Data_Extracted_" + sourceName + "_") && name.endsWith(".csv"));

            if (files == null || files.length < 2) {
                System.out.println("Not enough files to compare for " + sourceName);
                return;
            }

            // Sort files by timestamp (filename has the timestamp at the end)
            Arrays.sort(files, Comparator.comparing(File::getName));

            // Oldest and newest
            File oldFile = files[files.length - 2];
            File newFile = files[files.length - 1];

            System.out.println("Comparing " + sourceName + " files:");
            System.out.println("OLD → " + oldFile.getName());
            System.out.println("NEW → " + newFile.getName());

            // Compare product prices
            Map<String, String> oldPrices = readPrices(oldFile);
            Map<String, String> newPrices = readPrices(newFile);

            List<String> changedProducts = new ArrayList<>();
            for (Map.Entry<String, String> entry : newPrices.entrySet()) {
                String product = entry.getKey();
                String newPrice = entry.getValue();
                String oldPrice = oldPrices.get(product);

                if (oldPrice != null && !oldPrice.equals(newPrice)) {
                    changedProducts.add(product);
                }
            }

            if (!changedProducts.isEmpty()) {
                String changesFile = directory + "/Products_change_" + newFile.getName();
                writeChangedProducts(changedProducts, changesFile);
                System.out.println("Changes found → " + changesFile);
            } else {
                System.out.println("No price changes found for " + sourceName);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Map<String, String> readPrices(File file) {
        Map<String, String> prices = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean skipHeader = true;
            while ((line = br.readLine()) != null) {
                if (skipHeader || line.startsWith("Date and Time:")) {
                    skipHeader = false;
                    continue;
                }
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
                if (parts.length >= 3) {
                    String name = parts[0].replace("\"", "").trim();
                    String price = parts[2].replace("\"", "").trim();
                    prices.put(name, price);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prices;
    }

    private static void writeChangedProducts(List<String> products, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("Changed Products\n");
            for (String product : products) {
                writer.append(product).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
