require 'json'

package = JSON.parse(File.read(File.join(__dir__, '..', 'package.json')))

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

  # Swift/Objective-C compatibility
  s.pod_target_xcconfig = {
    'DEFINES_MODULE' => 'YES',
    'SWIFT_COMPILATION_MODE' => 'wholemodule'
  }

  s.source_files = "**/*.{h,m,swift}"
end
