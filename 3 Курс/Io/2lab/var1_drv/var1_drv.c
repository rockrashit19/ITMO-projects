#include <linux/module.h>
#include <linux/kernel.h>
#include <linux/fs.h>
#include <linux/uaccess.h>
#include <linux/cdev.h>
#include <linux/device.h>

#define DEVICE_NAME "var1"
static dev_t dev;
static struct cdev c_dev;
static struct class *cl;

#define BUF_LEN 1024
static char results[BUF_LEN];
static int count = 0;

static ssize_t my_read(struct file *f, char __user *buf, size_t len, loff_t *off)
{
    return simple_read_from_buffer(buf, len, off, results, strlen(results));
}

static ssize_t my_write(struct file *f, const char __user *buf, size_t len, loff_t *off)
{
    char tmp[BUF_LEN];
    if (len > BUF_LEN - 1) 
        return -EINVAL;

    if (copy_from_user(tmp, buf, len))
        return -EFAULT;

    tmp[len] = '\0';

    if (len > 0 && tmp[len - 1] == '\n') {
        tmp[len - 1] = '\0';
    }

    int chars = strlen(tmp);
    count += snprintf(results + count, BUF_LEN - count, "%d\n", chars);

    return len;
}


static struct file_operations fops =
{
    .owner = THIS_MODULE,
    .read = my_read,
    .write = my_write
};

static int __init var1_init(void)
{
    alloc_chrdev_region(&dev, 0, 1, DEVICE_NAME);
    cdev_init(&c_dev, &fops);
    cdev_add(&c_dev, dev, 1);
    cl = class_create(THIS_MODULE, "chardrv");
    device_create(cl, NULL, dev, NULL, "var1");
    printk(KERN_INFO "var1 driver loaded\n");
    return 0;
}

static void __exit var1_exit(void)
{
    device_destroy(cl, dev);
    class_destroy(cl);
    cdev_del(&c_dev);
    unregister_chrdev_region(dev, 1);
    printk(KERN_INFO "var1 driver unloaded\n");
}

module_init(var1_init);
module_exit(var1_exit);

MODULE_LICENSE("GPL");
MODULE_AUTHOR("Student");
MODULE_DESCRIPTION("Variant 1 Char Driver");
