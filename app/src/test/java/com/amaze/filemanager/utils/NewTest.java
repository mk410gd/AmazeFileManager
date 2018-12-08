package com.amaze.filemanager.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.widget.TextView;

import com.amaze.filemanager.BuildConfig;
import com.amaze.filemanager.R;
import com.amaze.filemanager.activities.TextEditorActivity;
import com.amaze.filemanager.utils.application.AppConfig;

import org.apache.commons.vfs2.provider.url.UrlFileName;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import org.robolectric.shadows.ShadowEnvironment;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, shadows = {ShadowMultiDex.class})
public class NewTest {

    private final String fileContents = "fsdfsdfs";
    private TextView text;

    @After
    public void tearDown() {
        AppConfig.getInstance().onTerminate();
    }

    @Test
    public void testOpenFileUri() throws IOException {
        File file = simulateFile();

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.fromFile(file));
        generateActivity(intent);

        assertThat(text.getText().toString(), is(fileContents + "\n"));
        assertEquals("text.txt",file.getName());
        assertTrue(file.canRead());
    }

    private void generateActivity(Intent intent) {
        ActivityController<TextEditorActivity> controller = Robolectric.buildActivity(TextEditorActivity.class, intent)
                .create().start().visible();

        TextEditorActivity activity = controller.get();
        text = activity.findViewById(R.id.fname);
        activity.onBackPressed();
    }

    private File simulateFile() throws IOException {
        ShadowEnvironment.setExternalStorageState(Environment.MEDIA_MOUNTED);
        File file = new File(Environment.getExternalStorageDirectory(), "text.txt");

        file.createNewFile();

        if(!file.canWrite()) file.setWritable(true);
        assertThat(file.canWrite(), is(true));

        PrintWriter out = new PrintWriter(file);
        out.write(fileContents);
        out.flush();
        out.close();

        return file;
    }
}