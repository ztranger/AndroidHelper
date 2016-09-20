package com.hpg.tools;

/**
 * Created by altunin on 29.08.2016.
 */
public class Constant {
    public final static String TAG = "HpgTools";
    public final static String PERMISSION_GRANTED = "PERMISSION_GRANTED";
    public final static String PERMISSION_DENIED = "PERMISSION_DENIED";

    public enum MemoryMeasure{
        B(0),
        Kb(1),
        Mb(2);
        private MemoryMeasure(int code)
        {
            this.code = code;
        }
        public int getStatusCode() {
            return code;
        }

        private int code;

        public float ConvertFromBytes(float data)
        {
            if(this == B)
                return data;
            return data/(code*1024f);
        }
        public float ConvertFromKb(float data)
        {
            switch(this)
            {
                case B:
                    return data * 1024;
                case Kb:
                    return data;
                case Mb:
                    return data / 1024;
            }
            return data / (1024f * (data - 1));
        }

        public float ConvertFromMb(float data)
        {
            switch(this)
            {
                case B:
                    return data * 1024f * 1024f;
                case Kb:
                    return data * 1024f;
                case Mb:
                    return data;
            }
            return data / (1024f * (data - 2));
        }
    }
}
