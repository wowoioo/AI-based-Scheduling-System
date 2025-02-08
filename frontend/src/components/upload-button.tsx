import React, { useRef, ForwardedRef } from 'react';
import { cva, type VariantProps } from "class-variance-authority";

const buttonVariants = cva(
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-md text-sm font-medium transition-colors focus-visible:outline-none focus-visible:ring-1 focus-visible:ring-ring disabled:pointer-events-none disabled:opacity-50",
    {
        variants: {
            variant: {
                default: "bg-primary text-primary-foreground shadow hover:bg-primary/90",
                destructive: "bg-destructive text-destructive-foreground shadow-sm hover:bg-destructive/90",
                outline: "border border-input bg-background shadow-sm hover:bg-accent hover:text-accent-foreground",
                secondary: "bg-secondary text-secondary-foreground shadow-sm hover:bg-secondary/80",
                ghost: "hover:bg-accent hover:text-accent-foreground",
                link: "text-primary underline-offset-4 hover:underline",
            },
            size: {
                default: "h-9 px-4 py-2",
                sm: "h-8 rounded-md px-3 text-xs",
                lg: "h-10 rounded-md px-8",
                icon: "h-9 w-9",
            },
        },
        defaultVariants: {
            variant: "default",
            size: "default",
        },
    }
);

interface ButtonProps extends React.ButtonHTMLAttributes<HTMLButtonElement>, VariantProps<typeof buttonVariants> {
    asChild?: boolean;
    onFileChange: (file: File | null) => void;
}

// 自定义的 UploadButton 组件
const UploadButton: React.FC<ButtonProps> = ({
                                                 className, variant, size, asChild = false, onFileChange, children, ...props
                                             }, ref: ForwardedRef<HTMLButtonElement>) => {
    const hiddenFileInput = useRef<HTMLInputElement>(null);

    const handleButtonClick = () => {
        if (hiddenFileInput.current) {
            console.log('Triggering file input click'); // 调试信息
            hiddenFileInput.current.click();
        }
    };

    const handleFileChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0] || null;
        console.log('Selected file:', file); // 调试信息
        onFileChange(file);
    };

    return (
        <>
            <button
                className={buttonVariants({ variant, size, className })}
                ref={ref}
                onClick={handleButtonClick}
                {...props}
            >
                {children || <span>Click to Upload</span>}
            </button>
            <input
                type="file"
                name="Course Excel"
                accept=".xlsx,.xls"
                onChange={handleFileChange}
                ref={hiddenFileInput}
                style={{ display: 'none' }}
            />
        </>
    );
};

UploadButton.displayName = "UploadButton";

export { UploadButton };