package controllers.CreateIndex;


import java.util.Map;

public class CreateIndexRequestBody
{
   private Map<String,PropertiesOfMapping> mappings;

    public Map<String, PropertiesOfMapping> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, PropertiesOfMapping> mappings) {
        this.mappings = mappings;
    }
}
