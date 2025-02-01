#
# To learn more about a Podspec see http://guides.cocoapods.org/syntax/podspec.html.
# Run `pod lib lint kmpble.podspec` to validate before publishing.
#
Pod::Spec.new do |s|
  s.name           = 'KMPBLE'
  s.version        = package['0.1.0']
  s.summary        = package['Cross Platform BLE']
  s.description    = package['Sherlock Blue KMPBLE']
  s.license        = package['license']
  s.author         = package['Janus Cole']
  s.homepage       = package['https://www.sherlockblue.com']
  s.platform       = :ios, '13.0'
  s.swift_version  = '5.4'
  s.source         = { :path '/Users/$username/.m2/repository/com/sherlockblue/KMPBLE/library-android/0.1.0' }
  s.static_framework = true
  s.source_files = 'Classes/**/*'
  s.dependency 'Flutter'

  # Flutter.framework does not contain a i386 slice.
  s.pod_target_xcconfig = { 'DEFINES_MODULE' => 'YES', 'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'i386' }

  # If your plugin requires a privacy manifest, for example if it uses any
  # required reason APIs, update the PrivacyInfo.xcprivacy file to describe your
  # plugin's privacy impact, and then uncomment this line. For more information,
  # see https://developer.apple.com/documentation/bundleresources/privacy_manifest_files
  # s.resource_bundles = {'kmpble_privacy' => ['Resources/PrivacyInfo.xcprivacy']}
end
