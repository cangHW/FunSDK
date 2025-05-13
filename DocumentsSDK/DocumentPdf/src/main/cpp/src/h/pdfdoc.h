
#include "fpdfview.h"


// 文档类
class DocumentFile {
private:
    int fileFd;

public:
    FPDF_DOCUMENT pdfDocument;

    DocumentFile();
    ~DocumentFile();
};

