package com.susu.baselibrary.utils.fileProvider;

import androidx.core.content.FileProvider;

/**
 * Author : sudan
 * Time : 2021/7/27
 * Description:
 * There can only be one {@link FileProvider} provider registered per app,
 * so we extend that class just to use a distinct name.
 * 自定义SuFileProvider，保证唯一性，避免和第三方库的FileProvider冲突
 */
public class SuFileProvider extends FileProvider {
}
