import React, { useEffect, useState } from 'react';
import { 
  View, 
  Text, 
  TextInput, 
  Alert, 
  TouchableOpacity, 
  NativeModules, 
  NativeEventEmitter, 
  Platform, 
  SafeAreaView, 
  StyleSheet, 
  Dimensions
} from 'react-native';

// Native Modules for Platform-Specific SDK
const { SDKBridge } = NativeModules; // iOS SDK
const MyCustomModule = NativeModules.XYZ; // Android SDK
const SDKBridgeEmitter = Platform.OS === 'ios' ? new NativeEventEmitter(SDKBridge) : null;
const windowWidth = Dimensions.get('window').width;

const App = () => {
  const [workflowId, setWorkflowId] = useState('');
  const [userId, setUserId] = useState('');

  // iOS: Handle Workflow Completion Event
  useEffect(() => {
    if (Platform.OS === 'ios' && SDKBridgeEmitter) {
      const eventListener = SDKBridgeEmitter.addListener('onWorkflowCompleted', (event) => {
        Alert.alert('GloRep', event.success, [
          {
            text: 'OK',
            onPress: () => {
              if (SDKBridge?.dismissNativeScreen) {
                SDKBridge.dismissNativeScreen(); 
              } else {
                console.error("SDKBridge.dismissNativeScreen is not available");
              }
            }
          }
        ]);
      });

      return () => eventListener.remove();
    }
  }, []);

  // Handle Verification - Platform-Specific Logic
  const handleVerification = () => {
    if (workflowId.trim() === '') {
      Alert.alert("Alert", "Please enter Workflow ID");
      return;
    }

    try {
      if (Platform.OS === 'ios') {
        console.log('Launching iOS SDK...');
        SDKBridge.launchGetStartedView(workflowId, userId.trim() === '' ? null : userId);
      } else if (Platform.OS === 'android') {
        console.log('Launching Android SDK...');
        MyCustomModule.showSDKLauncher(workflowId, userId.trim() === '' ? null : userId);
      } else {
        console.log('Unsupported platform');
      }
    } catch (err) {
      console.log('Error calling native module:', err);
    }
  };

  return (
    <SafeAreaView style={styles.container}>
      <View style={styles.inputContainer}>
        <Text style={styles.header}>User Verification</Text>
        <Text style={styles.label}>Workflow ID*</Text>
        <TextInput 
          style={styles.input}
          placeholder="Enter Workflow ID"
          onChangeText={setWorkflowId}
          value={workflowId}
        />
        <Text style={styles.label}>User ID</Text>
        <TextInput 
          style={styles.input}
          placeholder="Enter User ID"
          onChangeText={setUserId}
          value={userId}
        />
        <TouchableOpacity style={styles.button} onPress={handleVerification}>
          <Text style={styles.buttonText}>Start Verification</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

// Styles
const styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: '#F8F4F7',
    justifyContent: 'center',
    alignItems: 'center',
  },
  inputContainer: {
    width: windowWidth * 0.85, 
    maxWidth: 400,
    backgroundColor: 'white',
    padding: 20,
    borderRadius: 10,
    shadowColor: '#000',
    shadowOpacity: 0.1,
    shadowRadius: 10,
    elevation: 5,
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 20,
    textAlign: 'center',
  },
  label: {
    fontSize: 18,
    fontWeight: 'bold',
    color: '#000',
    marginBottom: 8,
  },
  input: {
    width: '100%',
    height: 50,
    borderWidth: 1,
    borderColor: '#E0E0E0',
    borderRadius: 8,
    paddingHorizontal: 15,
    fontSize: 16,
    marginBottom: 24,
    backgroundColor: '#FFF',
  },
  button: {
    backgroundColor: '#007AFF',
    paddingVertical: 15,
    borderRadius: 8,
    alignItems: 'center',
    marginTop: 20,
  },
  buttonText: {
    fontSize: 16,
    fontWeight: 'bold',
    color: '#FFF',
  }
});

export default App;
