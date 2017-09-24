package metadata;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import util.*;
import metadata.*;




public class Problem {

	/**
	 * The array of attributes wrapping their attribute values.
	 */
	private Attribute[] attributes;
	/**
	 * The index of the class attribute in the attribute array
	 */
	private int classAttributeIndex;
	/**
	 * The data of the data set
	 */
	private String[][] trainData;
	
	private String[][] testData;
	
	private List<String[]> data;
	
	/**
	 * The meta data of the data set
	 */
	private MetaData metadata;
	
	/**
	 * Initialize a data set
	 * @param baseName the base name of the input files (.names and .data).
	 */
	
	 public Problem(String fileName){
    	load(fileName);
        addColumnSetView();
    }
	 
	 public Problem(MetaData md) {
		 metadata = md;
		 this.classAttributeIndex = md.getClassAttributeIndex();
		 
		 
	 }
  /*  public Problem(String fileName, MetaData metaData){
    	int beginIndex = fileName.lastIndexOf("/")+1;
       	load(fileName,metaData);
    }*/
	
	
	
	
	private void load (String filePath)
	{
		setMetaData(loadMetaData(filePath + ".names"));
		setTrainData(loadData(filePath + ".data"));
		setTestData(loadData(filePath + ".test"));
		
	}
	
	
	
	public Set<Problem> divadeForIncrementalLearning(int part){
		data = ModifyFile.getFromArrayToList(trainData);
		Set<Problem> resSet = new LinkedHashSet<Problem>();
		int size = data.size();
		int partsize = size/part;
		int start=0;
		int end=partsize;
		int count =0;
		while(start!=end){
			count++;
			resSet.add(loadCVIncrement(start,end));
			start+=partsize;
			end+=partsize;
			
			if(end>size)
			{
				partsize = size-start;
				end = size;  
				
			}
		}
		
		return resSet;
	}
	
	
	public Set<Problem> divadeForIncrementalLearning(int part, Problem problem){
		problem.data = ModifyFile.getFromArrayToList(problem.getTrainData());
		Set<Problem> resSet = new LinkedHashSet<Problem>();
		int size = problem.data.size();
		int partsize = size/part;
		int start=0;
		int end=partsize;
		int count =0;
		while(start!=end){
			count++;
			resSet.add(loadCVIncrement(start,end,problem));
			start+=partsize;
			end+=partsize;
			
			if(end>size)
			{
				partsize = size-start;
				end = size;  
				
			}
		}
		
		return resSet;
	}
	
	
	
	public Problem add(Problem _problem){
		if(_problem == null) return this;
		data = ModifyFile.getFromArrayToList(trainData);
		_problem.data = ModifyFile.getFromArrayToList(trainData);
		List<String[]> res_data = new ArrayList<String[]>();
		res_data.addAll(data); res_data.addAll(_problem.data);
		
		Problem prob = new Problem(this.metadata);
		
		prob.setTrainData((String[][])res_data.toArray(new String[0][]));
		prob.setTestData(this.getTestData());
		prob.addColumnSetView();
		
		
		return prob;
		
	}
	
	
	
	public List<List<Problem>> loadCrossValidationIncLearn(int part)
	{
		data = ModifyFile.getSuffledData(trainData, testData);
		List<List<Problem>> resSet = new ArrayList<List<Problem>>();
		int size = data.size();
		int partsize = size/(part);
		int start=0;
		int end=partsize;
		int count =0;
		
		while(start!=end){
			count++;
			resSet.add(loadCVIncLearn(start,end,part));
			start+=partsize;
			end+=partsize;
			
			if(end>size)
			{
				partsize = size-start;
				end = size;  
				
			}
		}
		
		
		
		return resSet;
	}
	
	
	public List<Problem> loadCVIncLearn(int start, int end, int part)
	{
		//set train data
		List<String[]> train_data = new ArrayList<String[]>();
		List<String[]> test_data = new ArrayList<String[]>();
		
		test_data =  data.subList(start, end);
		train_data.addAll(data.subList(0, start));
		train_data.addAll(data.subList(end, data.size()));
		
		List<List<String[]>> t_data = ModifyFile.getParts(train_data, part);
		
		Iterator<List<String[]>> iter = t_data.iterator();
		List<Problem> resList = new ArrayList<Problem>();
		while(iter.hasNext()){
			Problem prob = new Problem(this.metadata);
			prob.setTrainData((String[][])iter.next().toArray(new String[0][]));
			prob.setTestData((String[][])test_data.toArray(new String[0][]));
			prob.addColumnSetView();
			resList.add(prob);
			
			
		}
		
		return resList;
	}
	
	
	public Set<Problem> loadCrossValidationWithMetaData(int part)
	{
		data =ModifyFile.getSuffledData(trainData, testData);
		Set<Problem> resSet = new LinkedHashSet<Problem>();
		int size = data.size();
		int partsize = size/part;
		int start=0;
		int end=partsize;
		int count =0;
		while(start!=end){
			count++;
			resSet.add(loadCV(start,end));
			start+=partsize;
			end+=partsize;
			
			if(end>size)
			{
				partsize = size-start;
				end = size;  
				
			}
		}
		
		return resSet;
	}
	
	
	public Problem loadCV(int start, int end)
	{
		//set train data
		List<String[]> train_data = new ArrayList<String[]>();
		List<String[]> test_data = new ArrayList<String[]>();
		
		test_data =  data.subList(start, end);
		train_data.addAll(data.subList(0, start));
		train_data.addAll(data.subList(end, data.size()));
		
		Problem prob = new Problem(this.metadata);
		
		prob.setTrainData((String[][])train_data.toArray(new String[0][]));
		prob.setTestData((String[][])test_data.toArray(new String[0][]));
		prob.addColumnSetView();
		
		return prob;
		
		
	}
	
	public Problem loadCVIncrement(int start, int end){
		//set train data
		List<String[]> train_data = new ArrayList<String[]>();
		
		train_data.addAll(data.subList(0, start));
		train_data.addAll(data.subList(end, data.size()));
		
		Problem prob = new Problem(this.metadata);
		
		prob.setTrainData((String[][])train_data.toArray(new String[0][]));
		prob.setTestData(testData);
		prob.addColumnSetView();
		
		return prob;
		
	}
	
	
	public Problem loadCVIncrement(int start, int end, Problem problem){
		//set train data
		List<String[]> train_data = new ArrayList<String[]>();
		
		train_data.addAll(problem.data.subList(0, start));
		train_data.addAll(problem.data.subList(end, problem.data.size()));
		
		Problem prob = new Problem(problem.metadata);
		
		prob.setTrainData((String[][])train_data.toArray(new String[0][]));
		prob.setTestData(problem.testData);
		prob.addColumnSetView();
		
		return prob;
		
	}
	
	
	
	
	public static Set<Problem> loadClossValidationWithoutMetaData (String filePath, int part)
	{
		Problem prob = new Problem (filePath);
		return prob.loadCrossValidationWithMetaData(part);
	}
	

	
	
	/**
	 * Load data from the specified file.
	 * @param filename the name of the input file
	 * @return The loaded data
     */
    public  String[][] loadData(String filename){
    	// Read in the test data in its original arrangement and extract its data fields
    	
        ArrayList<String[]> testList = new ArrayList<String[]>();
        try {
            BufferedReader bf = new BufferedReader(new FileReader(filename));
            String reader;
            for(; (reader = bf.readLine()) != null;) {
            	if(!reader.trim().equals("")) {
	                String[] line = extract(reader,",");
	                testList.add(line);
            	}
            }
            bf.close();
        }
        catch(IOException e){
            System.err.println(e);
        }

        // Transform the dynamic ArrayList to static Array
        return (String[][])testList.toArray(new String[0][]);
    }
	
    /**
	 * Extract the attribute values out from a single String separated with specified delimiter
	 * @param source the String to be extracted
	 * @param delimiterString the delimiter with which the attribute values are separated
	 * @return the extracted attribute values
	 */
    
	private static String[] extract(String source, String delimiterString) {
        String[] data = new String[source.length()];
        int count = 0;
        
        int splitPoint = 0;
        int j = 0;
        for(int length = source.length(); j < length; j ++){
        	// Compare char by char
            if (delimiterString.indexOf(source.charAt(j)) >= 0) {
                if (splitPoint != j) {
                    data[count ++] = source.substring(splitPoint, j).trim();
                    splitPoint = j;
                }
                splitPoint ++;
            }
        }
        if (splitPoint != j) {
            data[count ++] = source.substring(splitPoint, j).trim();
        }
		// Only number of "count" Strings are filled in data
        return Arrays.copyOf(data, count);
    }
    
	
	/**
	 * Load meta data from the specified file.
	 * @param filename the name of the input file
	 * @return The loaded meta data
     */
	public MetaData loadMetaData(String filename) {
		/* The 'tempAttributeNames' ArrayList dynamically reads the attribute names in;
		 * The 'tempIsContinuous' ArrayList dynamically reads their continuous properties in;
		 * The 'tempNominalValues' ArrayList dynamically reads the nominal values of the discrete attributes in;
		 */
     	ArrayList<String> tempAttributeNames = new ArrayList<String>();
     	ArrayList<Boolean> tempIsContinuous = new ArrayList<Boolean>();
     	ArrayList<String[]> tempNominalValues = new ArrayList<String[]>();

 		try {
 			/* The default class attribute name
 			 * If in the .names file, there is no "the target attribute : ****" sentences
 			 * The default class attribute name "class" is used
 			 */
        	String decisionAttribute = "class";
			//This variable records the index of attributes
        	int attributeIndex = 0;

        	BufferedReader bf = new BufferedReader(new FileReader(filename));
        	String reader;
 		    while((reader = bf.readLine()) != null) {
 		    	if(reader.trim().length() == 0) continue;

 	    		StringTokenizer token = new StringTokenizer(reader,":\t\n,.");
    		    String word = token.nextToken().trim();
	        	// Read in the target attribute name
		        if(word.equals("the target attribute")){
				    decisionAttribute = token.nextToken().trim();
		    	}
	        	else {
	        		// Record the index of the class attribute
	        		if(word.equals(decisionAttribute))  {
	        			setClassAttributeIndex(attributeIndex);
	        		}

	        		// Add attribute name
	        		tempAttributeNames.add(word);

	        		word = token.nextToken().trim();
	        		// Construct and add a ContinuousAttribute object
	        		if(word.equals("continuous")) {
						tempIsContinuous.add(true);
					/*	if(token.hasMoreTokens())
						{
							String[] values = new String[1];
							values[0]=token.nextToken().trim();
							tempNominalValues.add(values);
						}
					*/
						tempNominalValues.add(null);
						
	        		}
	        		// Construct and add a DiscreteAttribute object
	        		else{
	        			tempIsContinuous.add(false);
						// Get the nominal values of discrete attributes
	        			ArrayList<String> values = new ArrayList<String>();
	        			values.add(word);
	        			while(token.hasMoreTokens()) {
	        				values.add(token.nextToken().trim());
	        			}
	        			values.add("-"); // akamsi: added to have additional dont know value in discrete attribute
	        			tempNominalValues.add((String[])values.toArray(new String[0]));
	        		}

	        		attributeIndex ++;
	        	}
 		    }
 	    	bf.close();
 		}
 		catch(IOException e) {
 			System.err.println(e);
 		}

		// Transform the ArrayList to arrays
		String[] attributeNames = (String[])tempAttributeNames.toArray(new String[0]);
		String[][] nominalValues = (String[][])tempNominalValues.toArray(new String[0][]);
		boolean[] isContinuous = new boolean[tempIsContinuous.size()];
		for(int i = 0; i < isContinuous.length; i ++) {
			isContinuous[i] = tempIsContinuous.get(i);
		}

        MetaData m = new MetaData(attributeNames, isContinuous, nominalValues);
        m.setClassAttributeIndex(this.getClassAttributeIndex());
        
        return m;
	}

	
	
	
	
	
	  public void addColumnSetView(){
	    	int attributeCount = metadata.getAttributeCount();
			this.attributes = new Attribute[attributeCount];

			String[][] transposedTrainData = transpose(getTrainData());
			for(int i = 0; i < attributeCount; i ++) {
				String name = metadata.getAttributeNameAt(i);
				if(metadata.isAttributeContinuousAt(i)) {
					
						//String[] nominalValues = metaData.getAttributeNominalValuesAt(i);
						attributes[i] = new ContinuousAttribute(name, transposedTrainData[i]);
					
				}
				else{
					String[] nominalValues = metadata.getAttributeNominalValuesAt(i);
					attributes[i] = new DiscreteAttribute(name, nominalValues, transposedTrainData[i]);
				}
			}
	    }

		
		/**
		 * Get the meta data of the data set
		 */
	    public MetaData getMetaData() {
	    	return this.metadata;
	    }

		/**
		 * Set the meta data of the data set
		 */
	    public void setMetaData(MetaData metaData) {
	    	this.metadata = metaData;
	    }

	    /**
	     * Set the data of the data set
	     */
		public void setTrainData(String[][] trainData){
			this.trainData = trainData;
		}
		
		public void setTestData(String[][] testData){
			this.testData = testData;
		}
		
		/**
		 * Get the data of the data set
		 */
	    public String[][] getTrainData(){
	        return this.trainData;
	    }
	    
	    public String[][] getTestData(){
	        return this.testData;
	    }

		/**
		 * Set the attributes of the data set
		 */
	    public void setAttributes(Attribute[] attributes) {
	        this.attributes = attributes;
	    }

		/**
		 * Get the attributes of the data set
		 */
	    public Attribute[] getAttributes(){
	        return attributes;
	    }

		/**
		 * Get the index of the class attribute
		 */
		public int getClassAttributeIndex(){
			return classAttributeIndex;
		}

		/**
		 * Set the index of the class attribute
		 */
		public void setClassAttributeIndex(int classAttributeIndex) {
		    this.classAttributeIndex = classAttributeIndex;
		}

		/**
		 * Get the number of the data in the data set
		 */
		public int getCaseCount(){
			return this.trainData.length;
		}

		/**
		 * Get the number of the attributes in the data set
		 */
		public int getAttributeCount(){
			return attributes.length;
		}

	    /**
		 * Get the nominal values of the class attribute
		 */
		public String[] getClassValues(){
			return ((DiscreteAttribute)attributes[classAttributeIndex]).getNominalValues();
		}

		/**
		 * Get the number of different class values
		 */
		public int getClassCount(){
			return ((DiscreteAttribute)attributes[classAttributeIndex]).getNominalValuesCount();
		}

	    // Transpose a 2D String array
		private static String[][] transpose(String[][] from) {
			String[][] to = new String[from[0].length][from.length];
			for(int i = 0; i < from.length; i ++) {
				for(int j = 0; j < from[0].length; j ++) {
					to[j][i] = from[i][j];
				}
			}
			return to;
		}
	
	
	
	
}
