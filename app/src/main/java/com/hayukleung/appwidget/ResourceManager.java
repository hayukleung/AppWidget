package com.hayukleung.appwidget;

/**
 * SDK包资源读取方法 </br>
 * 替换所有R.XXX.XXX的获取资源ID方法
 *
 * chargerlink_v2
 * com.hayukleung.base
 * ResourceManager.java
 *
 * by hayukleung
 * at 2016-08-31 14:01
 */
public class ResourceManager {

  private static final String R_LAYOUT = "layout";
  private static final String R_DRAWABLE = "drawable";
  private static final String R_ID = "id";
  private static final String R_ANIM = "anim";
  private static final String R_RAW = "raw";
  private static final String R_STRING = "string";
  private static final String R_ATTR = "attr";
  private static final String R_ARRAY = "array";
  private static final String R_XML = "xml";
  private static final String R_MENU = "menu";

  /**
   * 以反射方式获取R.layout资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getLayoutResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_LAYOUT, name);
  }

  /**
   * 以反射方式获取R.id资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getIdResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_ID, name);
  }

  /**
   * 以反射方式获取R.drawable资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getDrawableResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_DRAWABLE, name);
  }

  /**
   * 以反射方式获取R.anim资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getAnimResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_ANIM, name);
  }

  /**
   * 以反射方式获取R.raw资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getRawResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_RAW, name);
  }

  /**
   * 以反射方式获取R.string资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getStringResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_STRING, name);
  }

  /**
   * 以反射方式获取R.attr资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getAttrResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_ATTR, name);
  }

  /**
   * 以反射方式获取R.array资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getArrayResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_ARRAY, name);
  }

  /**
   * 以反射方式获取R.xml资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getXmlResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_XML, name);
  }

  /**
   * 以反射方式获取R.menu资源ID
   *
   * @param packageName
   * @param name
   * @return
   */
  public static int getMenuResId(String packageName, String name) {
    return getResourceIdByName(packageName, R_MENU, name);
  }

  /**
   * http://stackoverflow.com/questions/14373004/java-lang-noclassdeffounderror-com-facebook-android-rlayout-error-when-using-f
   *
   * @param packageName
   * @param className
   * @param name
   * @return
   */
  private static int getResourceIdByName(String packageName, String className, String name) {

    Class<?> r;
    int id = 0;
    try {
      r = Class.forName(packageName + ".R");
      Class<?>[] classes = r.getClasses();
      Class<?> desireClass = null;
      for (int i = 0; i < classes.length; i++) {
        if (classes[i].getName().split("\\$")[1].equals(className)) {
          desireClass = classes[i];
          break;
        }
      }
      if (desireClass != null) id = desireClass.getField(name).getInt(desireClass);
    } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | SecurityException | IllegalArgumentException e) {
      e.printStackTrace();
    }
    return id;
  }
}
