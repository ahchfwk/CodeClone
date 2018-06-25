package code2token;


import java.awt.print.Printable;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class fwk403 {
	public static void main(String[] args) {
		Word word = new Word();
		try {
//			String str = word.segment("Hive.java").getTokenStr();
//			System.out.println(str);
			String myString = "public static Hive create(String hiveUri, String dimensionName, int indexType, HiveDataSourceProvider provider, Assigner assigner) {\r\n" + 
					"    Hive hive = prepareHive(hiveUri, provider, assigner, getDirectoryFacadeProvider());\r\n" + 
					"    PartitionDimension dimension = new PartitionDimension(dimensionName, indexType);\r\n" + 
					"    dimension.setIndexUri(hiveUri);\r\n" + 
					"    DataSource ds = provider.getDataSource(hiveUri);\r\n" + 
					"    PartitionDimensionDao dao = new PartitionDimensionDao(ds);\r\n" + 
					"    final List<PartitionDimension> partitionDimensions = dao.loadAll();\r\n" + 
					"    if (partitionDimensions.size() == 0) {\r\n" + 
					"      dao.create(dimension);\r\n" + 
					"      Schemas.install(new IndexSchema(dimension), dimension.getIndexUri());\r\n" + 
					"      hive.incrementAndPersistHive(ds);\r\n" + 
					"      return hive;\r\n" + 
					"    } else\r\n" + 
					"      (String.format(\"There is already a Hive with a partition dimension named %s intalled at this uri: %s\", Atom.getFirstOrThrow(partitionDimensions).getName(), hiveUri));\r\n" + 
					"  }\r\n" + 
					"";
			//String str2 = word.segment("{\n    super.start(context);    module = this;}", true).getTokenStr();
			String str1 = word.segment("{    super.start(context);    module = this;}", true).lineTokenNumList.toString();
			String str2 = word.segment("{\r\n super.start(context);\r\n  module = this;\r\n}", true).lineTokenNumList.toString();
			String str3 = word.segment("{\n   super.start(context);\n    module = this;\n}", true).lineTokenNumList.toString();
			System.out.println(str1);
			System.out.println(str2);
			System.out.println(str3);
			//word.save("fwk.txt");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
