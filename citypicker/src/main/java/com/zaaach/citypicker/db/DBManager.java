package com.zaaach.citypicker.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.ProCityBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.zaaach.citypicker.db.DBConfig.COLUMN_AREA_CODE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_AREA_ID;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_AREA_NAME;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_AREA_PARENT_ID;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_CODE;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_NAME;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PINYIN;
import static com.zaaach.citypicker.db.DBConfig.COLUMN_C_PROVINCE;
import static com.zaaach.citypicker.db.DBConfig.DB_NAME_V2;
import static com.zaaach.citypicker.db.DBConfig.LATEST_DB_NAME;
import static com.zaaach.citypicker.db.DBConfig.DB_NAME_V1;
import static com.zaaach.citypicker.db.DBConfig.NEW_TABLE_NAME;
import static com.zaaach.citypicker.db.DBConfig.TABLE_NAME;

/**
 * Author Bro0cL on 2016/1/26.
 */
public class DBManager {
    private static final int BUFFER_SIZE = 1024;

    private String DB_PATH;
    private Context mContext;

    public DBManager(Context context) {
        this.mContext = context;
        DB_PATH = File.separator + "data"
                + Environment.getDataDirectory().getAbsolutePath() + File.separator
                + context.getPackageName() + File.separator + "databases" + File.separator;
        copyDBFile();
    }

    private void copyDBFile() {
        File dir = new File(DB_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        //如果旧版数据库存在，则删除
        File dbV1 = new File(DB_PATH + DB_NAME_V1);
        if (dbV1.exists()) {
            dbV1.delete();
        }
        File dbV2 = new File(DB_PATH + DB_NAME_V2);
        if (dbV2.exists()) {
            dbV2.delete();
        }
        //创建新版本数据库
        File dbFile = new File(DB_PATH + LATEST_DB_NAME);
        if (!dbFile.exists()) {
            InputStream is;
            OutputStream os;
            try {
                is = mContext.getResources().getAssets().open(LATEST_DB_NAME);
                os = new FileOutputStream(dbFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int length;
                while ((length = is.read(buffer, 0, buffer.length)) > 0) {
                    os.write(buffer, 0, length);
                }
                os.flush();
                os.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public List<City> getAllCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + TABLE_NAME, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            city = new City(name, province, pinyin, code);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    public List<City> getAllProvince() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{"0"});

        Cursor cursor1 = db.rawQuery("select * from " + NEW_TABLE_NAME, null);
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
            String parent_id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
            city = new City(name, parent_id, code, id);
            result.add(city);
        }
        cursor.close();
        db.close();
//        Collections.sort(result, new CityComparator());
        return result;
    }

    public List<ArrayList<City>> getAllProvinceCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{"0"});
        List<ArrayList<City>> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
            Cursor cursor1 = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{id});
            ArrayList<City> result1 = new ArrayList<>();
            while (cursor1.moveToNext()) {
                String name1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
                String parent_id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
                String id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
                String code1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
                city = new City(name1, parent_id1, code1, id1);
                result1.add(city);
            }
            result.add(result1);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<ArrayList<City>> getAllProvinceCities1() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{"0"});
        List<ArrayList<City>> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
            String parent_id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
            city = new City(name, parent_id, code, id);
            Cursor cursor1 = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{id});
            ArrayList<City> result1 = new ArrayList<>();
            result1.add(city);
            while (cursor1.moveToNext()) {
                String name1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
                String parent_id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
                String id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
                String code1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
                city = new City(name1, parent_id1, code1, id1);
                result1.add(city);
            }
            result.add(result1);
        }
        cursor.close();
        db.close();
        return result;
    }

    public List<ProCityBean> getAllProCities() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{"0"});
        List<ProCityBean> proResult = new ArrayList<ProCityBean>();
        City topCity = new City("全国", "0000", "000000", "0");
        ArrayList<City> topArrayList = new ArrayList<>();
        topArrayList.add(topCity);
        proResult.add(new ProCityBean(true, "0", "全国", "00000", topArrayList));
        ProCityBean proCityBean;
        while (cursor.moveToNext()) {
            String proId = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
            String proName = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
            String proCode = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
            Cursor cursor1 = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " = ?", new String[]{proId});
            ArrayList<City> cityArrayList = new ArrayList<>();
            City city;
            while (cursor1.moveToNext()) {
                String name1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
                String parent_id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
                String id1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
                String code1 = cursor1.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
                city = new City(name1, parent_id1, code1, id1);
                cityArrayList.add(city);
            }
            proCityBean = new ProCityBean(false, proId, proName, proCode, cityArrayList);
            proResult.add(proCityBean);
        }
        cursor.close();
        db.close();
        return proResult;
    }


    public List<City> getAllCitiesArea() {
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where " + COLUMN_AREA_PARENT_ID + " like ?", new String[]{"%0000"});
        List<City> result = new ArrayList<>();
        City city;
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
            String parent_id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
            String id = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_ID));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_CODE));
            city = new City(name, parent_id, code, id);
            result.add(city);
        }
        cursor.close();
        db.close();
        Collections.sort(result, new CityComparator());
        return result;
    }

    public String getAreaName(final String id) {
        String areaName = "城市";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select " + COLUMN_AREA_NAME + " from " + NEW_TABLE_NAME + " where "
                + COLUMN_AREA_ID + " = ?", new String[]{id});
        while (cursor.moveToNext()) {
            areaName = cursor.getString(0);
        }
        return areaName;
    }

    public String getAreaNames(final String id) {
        String proName = "省份";
        String cityName = "城市";
        String areaParentID = "110000";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery("select * from " + NEW_TABLE_NAME + " where "
                + COLUMN_AREA_ID + " = ?", new String[]{id});
        while (cursor.moveToNext()) {
            cityName = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_NAME));
            areaParentID = cursor.getString(cursor.getColumnIndex(COLUMN_AREA_PARENT_ID));
        }
        Cursor cursor1 = db.rawQuery("select " + COLUMN_AREA_NAME + " from " + NEW_TABLE_NAME + " where "
                + COLUMN_AREA_ID + " = ?", new String[]{areaParentID});
        while (cursor1.moveToNext()) {
            proName = cursor1.getString(cursor1.getColumnIndex(COLUMN_AREA_NAME));
        }
        return proName+"-"+cityName;
    }

    public List<City> searchCity(final String keyword) {
        String sql = "select * from " + TABLE_NAME + " where "
                + COLUMN_C_NAME + " like ? " + "or "
                + COLUMN_C_PINYIN + " like ? ";
        SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(DB_PATH + LATEST_DB_NAME, null);
        Cursor cursor = db.rawQuery(sql, new String[]{"%" + keyword + "%", keyword + "%"});

        List<City> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex(COLUMN_C_NAME));
            String province = cursor.getString(cursor.getColumnIndex(COLUMN_C_PROVINCE));
            String pinyin = cursor.getString(cursor.getColumnIndex(COLUMN_C_PINYIN));
            String code = cursor.getString(cursor.getColumnIndex(COLUMN_C_CODE));
            City city = new City(name, province, pinyin, code);
            result.add(city);
        }
        cursor.close();
        db.close();
        CityComparator comparator = new CityComparator();
        Collections.sort(result, comparator);
        return result;
    }

    /**
     * sort by a-z
     */
    private class CityComparator implements Comparator<City> {
        @Override
        public int compare(City lhs, City rhs) {
            String a = lhs.getPinyin().substring(0, 1);
            String b = rhs.getPinyin().substring(0, 1);
            return a.compareTo(b);
        }
    }
}
