package POJO.JSON;
public class FingerImage {
   private String path;
   private String public_url;

   public FingerImage(String path, String public_url){
       this.path=path;
       this.public_url=public_url;
   }
    public String getPath() {
        return path;
    }

    public String getPublic_url() {
        return public_url;
    }     
}
