TemplateOcrDemo
==============================

The TemplateOcrDemo app demonstrates how to use Template OCR.

Introduction
------------

- [Read more about HUAWEI HiAI Engine](https://developer.huawei.com/consumer/cn/doc/development/hiai-Guides/31403)

Getting Started
---------------

- [Add HUAWEI HiAI Engine SDK to your Android Project](https://developer.huawei.com/consumer/cn/doc/development/hiai-Guides/31403)
- Run the sample on Android device or emulator.

Prepare
---------------------

Prepare a template image according to business needs, for example:template-image.jpg

Process
-----------

Install the plug-in of making template json in Android Studio or DevEco Studio,
then load the template image to mark the reference area and recognition area through the plug-in
sketch map is template-image-after-designated-area.jpg，The fixed field marked by the green vertex
box is the reference area, and the variable text marked by the red box is the recognition area.
after marking, you can export a template json file, for example:template.json

Usage
-----------

Input the actual picture provided by the user and the corresponding template json into the
Template OCR interface at the same time, and then the recognition result of the recognition area
can be returned.
 
Test pictures
-----------

Here are three test pictures:test-1.jpg,test-2.jpg,test-3.jpg, In this demo, you can switch the
recognition pictures and view the recognition results through the buttons