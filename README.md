# AI-Powered Kids Drawing Analysis 🎨🤖

An end-to-end Machine Learning project that leverages **Deep Learning** and **Computer Vision** to analyze children's drawings for psychological and developmental insights. This repository contains the complete ML pipeline—from data preprocessing and model training to evaluation—along with the Android application serving as the production environment for the model.

## 🧠 Machine Learning Architecture & Pipeline

This project demonstrates a robust approach to image classification using transfer learning, specifically tailored for the nuanced task of analyzing children's artwork.

### 1. Data Pipeline & Augmentation
- **Dynamic Augmentation**: Utilized `tf.keras.preprocessing.image.ImageDataGenerator` to prevent overfitting and improve model generalization on drawing datasets.
- **Transformations Applied**: Rotation, width/height shifting, zooming, shearing, and brightness adjustments.
- **Stratified Splitting**: Ensured balanced class distribution across training, validation, and testing sets using `scikit-learn`.

### 2. Model Architecture (Transfer Learning)
- **Base Model**: **EfficientNetB3** (Pre-trained on ImageNet). Chosen for its optimal balance between parameter efficiency and high accuracy, making it highly suitable for mobile/edge deployment.
- **Custom Classification Head**: 
  - `Flatten()` layer for feature map reduction.
  - `Dropout(0.3)` layer to enforce regularization and mitigate overfitting.
  - `Dense(Softmax)` layer dynamically scaled to the number of drawing classes.

### 3. Training Strategy
- **Optimizer**: Adam
- **Loss Function**: Categorical Crossentropy
- **Callbacks**: Implemented `EarlyStopping` (patience=10) with best-weight restoration to ensure optimal convergence without over-training.

### 4. Model Evaluation & Metrics
- Generated comprehensive **Classification Reports** to analyze precision, recall, and f1-score per class.
- Visualized performance using **Confusion Matrices** (via Seaborn) to identify class-specific misclassifications.
- Tracked epoch-over-epoch accuracy and loss curves for both training and validation phases.

## 📱 Application & Edge Deployment

While the core of the project is the deep learning model, it includes a fully functional native Android application designed as the production environment for the AI.
- **AI Integration**: Consumes the trained model to process and evaluate drawings, presenting results to authorized medical professionals.
- **Multi-Role System**: Features secure, specialized dashboards for Doctors, Parents, Children, and Admins.
- **Communication Hub**: Includes a built-in scheduling system and live chat/discussion groups (powered by Firebase) to facilitate parent-doctor consultations based on the AI's findings.

## 📂 Repository Structure

- `ml_model/`: Core AI directory containing the training pipeline.
  - `analysis_of_drawing_of_children.py`: The main training, augmentation, and evaluation script.
  - `requirements.txt`: Python dependencies.
- `app/`: The production Android application (Java/XML) that integrates the trained `.h5` model to provide a multi-role interface for Doctors, Parents, and Admins.

## 🚀 Getting Started (ML Pipeline)

1. Navigate to the model directory:
   ```bash
   cd ml_model
   ```
2. Install the required AI dependencies:
   ```bash
   pip install -r requirements.txt
   ```
3. Run the training pipeline:
   ```bash
   python analysis_of_drawing_of_children.py
   ```
*(Note: Requires the dataset path to be configured in the script. The script outputs `my_model_analysis.h5` upon completion).*

## 🛠️ AI Tech Stack
- **Deep Learning Framework**: TensorFlow, Keras
- **Computer Vision Model**: EfficientNetB3
- **Data Processing & Evaluation**: Scikit-Learn, Pandas, NumPy, Matplotlib, Seaborn
- **Production Edge Environment**: Android (Java), Firebase
