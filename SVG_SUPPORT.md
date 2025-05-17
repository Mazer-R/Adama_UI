# SVG Support in JavaFX

This document provides information about the SVG support implementation in the Adama UI application.

## Overview

The implementation uses the [javafxsvg](https://github.com/codecentric/javafxsvg) library to render SVG files in JavaFX. This library allows SVG files to be loaded directly as JavaFX `Image` objects, which can then be used in `ImageView` components.

## Implementation Details

The following changes were made to add SVG support:

1. Added the javafxsvg library dependency to pom.xml:
   ```xml
   <dependency>
       <groupId>de.codecentric.centerdevice</groupId>
       <artifactId>javafxsvg</artifactId>
       <version>1.3.0</version>
   </dependency>
   ```

2. Updated module-info.java to include the javafxsvg module:
   ```java
   requires javafxsvg;
   ```

3. Created a utility class `SvgLoader` to help with loading SVG icons:
   ```java
   package com.example.adama_ui.util;

   import de.codecentric.centerdevice.javafxsvg.SvgImageLoaderFactory;
   import javafx.scene.image.Image;
   import javafx.scene.image.ImageView;

   public class SvgLoader {
       private static boolean initialized = false;
       
       public static void initialize() {
           if (!initialized) {
               SvgImageLoaderFactory.install();
               initialized = true;
           }
       }
       
       public static Image loadSvgImage(String path) {
           if (!initialized) {
               initialize();
           }
           return new Image(SvgLoader.class.getResourceAsStream(path));
       }
       
       public static ImageView loadSvgImageView(String path) {
           return new ImageView(loadSvgImage(path));
       }
       
       public static ImageView loadSvgImageView(String path, double width, double height) {
           ImageView imageView = loadSvgImageView(path);
           imageView.setFitWidth(width);
           imageView.setFitHeight(height);
           imageView.setPreserveRatio(true);
           return imageView;
       }
   }
   ```

4. Updated the main application class to initialize the SVG loader:
   ```java
   // In StartAdamaApp.java
   @Override
   public void start(Stage stage) throws IOException {
       // Initialize SVG loader
       SvgLoader.initialize();
       
       // Rest of the method...
   }
   ```

5. Created a demo application `SvgIconsDemo` to test SVG icon loading.

## Usage

### Initialization

The SVG loader is automatically initialized when the application starts, so you don't need to initialize it again.

### Loading SVG Icons

You can use the `SvgLoader` utility class to load SVG icons:

```java
// Load an SVG file as an Image
Image svgImage = SvgLoader.loadSvgImage("/ExternalResources/featherIcons/home.svg");

// Load an SVG file as an ImageView
ImageView svgImageView = SvgLoader.loadSvgImageView("/ExternalResources/featherIcons/home.svg");

// Load an SVG file as an ImageView with specific dimensions
ImageView svgImageView = SvgLoader.loadSvgImageView("/ExternalResources/featherIcons/home.svg", 24, 24);
```

### Using SVG Icons in FXML

You can use SVG icons directly in FXML files:

```xml
<ImageView>
    <Image url="@/ExternalResources/featherIcons/home.svg"/>
</ImageView>
```

The javafxsvg library will automatically handle loading and rendering the SVG file.

## Demo

A demo application is provided to test SVG icon loading. You can run the `SvgIconsDemo` class to see SVG icons in action.

## Available Icons

SVG icons are stored in the `/ExternalResources/featherIcons/` directory. These are [Feather Icons](https://feathericons.com/), a collection of simple, open-source icons.