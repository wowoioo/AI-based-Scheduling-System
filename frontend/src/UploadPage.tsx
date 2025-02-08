import React, {useRef} from "react";
import {Button} from "@/components/ui/button";
import {downloadExcel, uploadExcel, uploadResultExcel} from "./api";
import ClassroomTable from "./ClassroomTable";
import {Sheet, SheetTrigger, SheetContent, SheetHeader, SheetTitle, SheetDescription} from "@/components/ui/sheet";
import {Separator} from "./components/ui/separator";
import {Input} from "./components/ui/input";
import {Download} from "lucide-react";
import {Upload} from 'lucide-react';
import {UploadButton} from "./components/upload-button"

const UploadExcel: React.FC = () => {
    const handleFileChange = async (file: File | null) => {
        if (!file) return;

        try {
            await uploadExcel(file);
            window.location.reload();
        } catch (error) {
            console.error("Upload failed:", error);
            alert("Upload failed");
        }
    };

    return (
        <div>
            <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white" htmlFor="file_input">
                Upload Excel
            </label>
            <UploadButton onFileChange={handleFileChange}>
                <Upload className="mr-2 h-4 w-4" /> Scheduling Upload
            </UploadButton>
        </div>
    );
};

const UploadResultExcel: React.FC = () => {
    const handleFileChange = async (file: File | null) => {
        if (!file) return;

        try {
            await uploadResultExcel(file);
            window.location.reload();
        } catch (error) {
            console.error("Upload failed:", error);
            alert("Upload failed");
        }
    };

    return (
        <div>
            <label className="block mb-2 text-sm font-medium text-gray-900 dark:text-white" htmlFor="file_input">
                Result Upload
            </label>
            <UploadButton onFileChange={handleFileChange}>
                <Upload className="mr-2 h-4 w-4" /> Result Upload
            </UploadButton>
        </div>
    );
};

const UploadPage: React.FC = () => {
    const classroomTableRef = useRef<{ add: () => void } | null>(null);

    const handleAddRow = () => {
        if (classroomTableRef.current) {
            classroomTableRef.current.add();
        }
    };

    const downloadFile = async () => {
        try {
            await downloadExcel();
        } catch (error) {
            console.error("Download failed:", error);
            alert("Download failed");
        }
    };

    return (
        <div className="flex flex-wrap md:flex-row flex-col items-end md:justify-start justify-center gap-4">
            <div className="flex items-end gap-4">
                <UploadExcel />
                <Button onClick={downloadFile} className="flex items-center gap-2">
                    <Download />
                    Export Excel
                </Button>
            </div>
            <div className="flex items-end gap-4">
                <UploadResultExcel />
                <Sheet>
                    <SheetTrigger asChild>
                        <Button>Classroom Config</Button>
                    </SheetTrigger>
                    <SheetContent className="w-[640px] max-w-full sm:max-w-[640px] gap-4 flex flex-col">
                        <SheetHeader>
                            <SheetTitle>Classroom Information</SheetTitle>
                        </SheetHeader>
                        <Separator />
                        <div className="flex flex-row w-full gap-4">
                            <Button onClick={handleAddRow}>Add new classroom</Button>
                            <SheetDescription />
                        </div>
                        <ClassroomTable ref={classroomTableRef} />
                    </SheetContent>
                </Sheet>
            </div>
        </div>
    );

};

export default UploadPage;
