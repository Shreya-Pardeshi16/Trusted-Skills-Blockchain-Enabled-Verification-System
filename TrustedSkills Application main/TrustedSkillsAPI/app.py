from flask import Flask, request, jsonify
from azure.cognitiveservices.vision.computervision import ComputerVisionClient
from azure.cognitiveservices.vision.computervision.models import OperationStatusCodes
from msrest.authentication import CognitiveServicesCredentials
from pdfminer.high_level import extract_text
from werkzeug.utils import secure_filename
import time
import os
from flask_cors import CORS
# -----------------------------
# Flask setup
# -----------------------------
app = Flask(__name__) 
CORS(app)  #   fixes CORS
UPLOAD_FOLDER = "uploads"
os.makedirs(UPLOAD_FOLDER, exist_ok=True)
app.config["UPLOAD_FOLDER"] = UPLOAD_FOLDER

# -----------------------------
# Azure OCR setup
# -----------------------------
AZURE_KEY = "2fefe16f95c1409eb80bf49c84064e27"
AZURE_ENDPOINT = "https://sohamvision.cognitiveservices.azure.com/"
cv_client = ComputerVisionClient(AZURE_ENDPOINT, CognitiveServicesCredentials(AZURE_KEY))

# -----------------------------
# Helper: extract text from file
# -----------------------------
def extract_text_from_file(file_stream, filename):
    text = ""

    if filename.lower().endswith((".jpg", ".jpeg", ".png", ".bmp", ".tiff")):
        # Azure OCR
        read_response = cv_client.read_in_stream(file_stream, raw=True)
        operation_location = read_response.headers["Operation-Location"]
        operation_id = operation_location.split("/")[-1]

        while True:
            result = cv_client.get_read_result(operation_id)
            if result.status not in ['notStarted', 'running']:
                break
            time.sleep(1)

        if result.status == OperationStatusCodes.succeeded:
            for page in result.analyze_result.read_results:
                for line in page.lines:
                    text += line.text + " "

    elif filename.lower().endswith(".pdf"):
        file_stream.seek(0)
        text = extract_text(file_stream)
    # ---------- TXT ----------
    elif filename.endswith(".txt"):
        file_stream.seek(0)
        text = file_stream.read().decode("utf-8", errors="ignore")

    # ---------- DOC / DOCX ----------
    elif filename.endswith((".doc", ".docx")):
        file_stream.seek(0)
        document = docx.Document(file_stream)
        text = " ".join(p.text for p in document.paragraphs)

    else:
        raise ValueError("Unsupported file type")

    return text.strip().lower()


# -----------------------------
# Step 1: Validate Document
# -----------------------------
@app.route("/validateDocument", methods=["POST"])
def validate_document():
    print("in validate")
    file = request.files.get("file")
    description = request.form.get("description", "")
    username = request.form.get("username", "").lower()
    print("description")   
    print(description)
    print(username)
    if not file:
        return jsonify({"valid": False, "error": "No file uploaded"}), 400

    try:
        # Extract text from file
        extracted_text = extract_text_from_file(file.stream, file.filename)
        #print(extracted_text)
        # Check username exists in document
        if username not in extracted_text:
            return jsonify({"valid": False, "reason": "Username not found"})

        # Check description keywords
        doc_words = set(extracted_text.split())
        desc_words = set(description.lower().split())
        matches = len(doc_words & desc_words)
        print("match summary")
        print(max(1, len(desc_words)//2))
        print(matches)
        valid = matches >= max(1, len(desc_words)//2)  # at least half of keywords must match
        print(matches)
        print(str(jsonify({"valid": valid, "matchedKeywords": matches})))
        return jsonify({"valid": valid, "matchedKeywords": matches})

    except Exception as e:
        print(e)
        return jsonify({"valid": False, "error": str(e)}), 500


# -----------------------------
# Step 2: Upload Document
# -----------------------------
@app.route("/UploadDocument", methods=["POST"])
def upload_document():
    file = request.files.get("documentFile")
    title = request.form.get("title", "")
    description = request.form.get("docdesc", "")
    username = request.form.get("userid", "")

    if not file:
        return jsonify({"error": "No file uploaded"}), 400

    try:
        filename = secure_filename(file.filename)
        save_path = os.path.join(app.config["UPLOAD_FOLDER"], filename)
        file.save(save_path)

        # You can add DB insert logic here

        return jsonify({"success": True, "filename": filename})

    except Exception as e:
        return jsonify({"error": str(e)}), 500


# -----------------------------
# Run Flask
# -----------------------------
if __name__ == "__main__":
    app.run(debug=True, port=5000)
