/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vvmarkets.core;

import javafx.scene.image.Image;

/**
 *
 * @author sharif
 */
public interface IListContent {
    String getName();
    String getQueryId();
    ListContentType getType();
    Image getThumb();
}
