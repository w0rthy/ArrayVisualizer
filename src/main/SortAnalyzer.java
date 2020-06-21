package main;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ScanResult;

import templates.Sort;
import utils.Delays;
import utils.Highlights;
import utils.Reads;
import utils.Writes;

/*
 * 
The MIT License (MIT)

Copyright (c) 2019 Luke Hutchison

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
 *
 */

final public class SortAnalyzer {
    private ArrayList<Sort> comparisonSorts;
    private ArrayList<Sort> distributionSorts;
    private ArrayList<String> invalidSorts;
    
    private Delays Delays;
    private Highlights Highlights;
    private Reads Reads;
    private Writes Writes;
    
    public SortAnalyzer(ArrayVisualizer ArrayVisualizer) {
        this.comparisonSorts = new ArrayList<>();
        this.distributionSorts = new ArrayList<>();
        this.invalidSorts = new ArrayList<>();
        
        this.Delays = ArrayVisualizer.getDelays();
        this.Highlights = ArrayVisualizer.getHighlights();
        this.Reads = ArrayVisualizer.getReads();
        this.Writes = ArrayVisualizer.getWrites();
    }
    
    public void analyzeSorts() {
        try (ScanResult scanResult = new ClassGraph().whitelistPackages("sorts").scan()) {
            List<ClassInfo> sortFiles;
            sortFiles = scanResult.getAllClasses();
            
            for(int i = 0; i < sortFiles.size(); i++) {
                try {
                    Class<?> sortClass = Class.forName(sortFiles.get(i).getName());
                    Constructor<?> newSort = sortClass.getConstructor(new Class[] {Delays.class, Highlights.class, Reads.class, Writes.class});
                    Sort sort = (Sort) newSort.newInstance(this.Delays, this.Highlights, this.Reads, this.Writes);

                    try {
                        if(verifySort(sort)) {
                            if(sort.comparisonBased()) {
                                comparisonSorts.add(sort);
                            }
                            else {
                                distributionSorts.add(sort);
                            }
                        }
                        else {
                            throw new Exception(sort.getClass().getName() + " is not a valid algorithm!");
                        }
                    }
                    catch(Exception e) {
                        invalidSorts.add(sort.getClass().getName());
                        e.printStackTrace();
                    }
                }
                catch(Exception e) {
                    invalidSorts.add(sortFiles.get(i).getName());
                    e.printStackTrace();
                }
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    
    private static boolean verifySort(Sort sort) {
        boolean validSort = true;
        
        if(sort.getSortPromptID().equals("")) {
            System.out.println(sort.getClass().getName() + " does not have a prompt ID! ");
            validSort = false;
        }
        if(sort.getRunAllID().equals("")) {
            System.out.println(sort.getClass().getName() + " does not have a 'Run All Sorts' ID! ");
            validSort = false;
        }
        if(sort.getReportSortID().equals("")) {
            System.out.println(sort.getClass().getName() + " does not have a 'Run Sort' ID! ");
            validSort = false;
        }
        if(sort.getCategory().equals("")) {
            System.out.println(sort.getClass().getName() + " does not have a category! ");
            validSort = false;
        }
        
        if(sort.getUnreasonablySlow() && sort.getUnreasonableLimit() == 0) {
            System.out.println(sort.getClass().getName() + " is marked as an unreasonably slow sort, yet does not have a threshold for a warning message! ");
            validSort = false;
        }
        if(!sort.getUnreasonablySlow() && sort.getUnreasonableLimit() != 0) {
            System.out.println(sort.getClass().getName() + " is not marked as an unreasonably slow sort, yet has an unreasonably slow limit other than zero. ");
            sort.setUnreasonableLimit(0);
        }
        
        if(!sort.getUnreasonablySlow() && sort.bogoSort()) {
            System.out.println(sort.getClass().getName() + " is a type of Bogosort, yet is not labeled as unreasonably slow! ");
            validSort = false;
        }
        
        if(sort.radixSort() && !sort.usesBuckets()) {
            System.out.println(sort.getClass().getName() + " is a radix sort and should also be classified as a bucket sort. ");
            sort.isBucketSort(true);
        }
        
        if(sort.radixSort() && sort.comparisonBased()) {
            System.out.println(sort.getClass().getName() + " is a radix sort that is incorrectly labeled as a comparison sort! ");
            validSort = false;
        }
        
        return validSort;
    }
    
    public String[][] getComparisonSorts() {
        String[][] ComparisonSorts = new String[2][comparisonSorts.size()];
        
        for(int i = 0; i < ComparisonSorts[0].length; i++) {
            ComparisonSorts[0][i] = comparisonSorts.get(i).getClass().getName();
            ComparisonSorts[1][i] = comparisonSorts.get(i).getSortPromptID();
        }
        
        return ComparisonSorts;
    }
    public String[][] getDistributionSorts() {
        String[][] DistributionSorts = new String[2][distributionSorts.size()];
        
        for(int i = 0; i < DistributionSorts[0].length; i++) {
            DistributionSorts[0][i] = distributionSorts.get(i).getClass().getName();
            DistributionSorts[1][i] = distributionSorts.get(i).getSortPromptID();
        }
        return DistributionSorts;
    }
    public String[] getInvalidSorts() {
        if(invalidSorts.size() < 1) {
            return null;
        }
        
        String[] InvalidSorts = new String[invalidSorts.size()];
        InvalidSorts = invalidSorts.toArray(InvalidSorts);
        
        return InvalidSorts;
    }
}